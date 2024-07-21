// Impl.SeatQueryPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.SeatQueryResponse
import Common.{SeatInfo, SeatStatus}

case class SeatQueryPlanner(floor: String, section: String, seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber)
      )
    ).map {
      case Nil =>
        SeatQueryResponse(None).asJson.noSpaces
      case rows =>
        rows.headOption.map { row =>
          SeatInfo(
            floor = row.hcursor.get[String]("floor").getOrElse(""),
            section = row.hcursor.get[String]("section").getOrElse(""),
            seatNumber = row.hcursor.get[String]("seat_number").getOrElse(""),
            status = row.hcursor.get[String]("status").flatMap(SeatStatus.fromString).getOrElse(SeatStatus.Available),
            feedback = row.hcursor.get[String]("feedback").getOrElse(""),
            occupied = row.hcursor.get[Boolean]("occupied").getOrElse(false),
            studentNumber = row.hcursor.get[String]("student_number").getOrElse("")
          )
        }.map(seat => SeatQueryResponse(Some(seat)).asJson.noSpaces).getOrElse(SeatQueryResponse(None).asJson.noSpaces)
    }.handleError(error => SeatQueryResponse(None).asJson.noSpaces)
}
