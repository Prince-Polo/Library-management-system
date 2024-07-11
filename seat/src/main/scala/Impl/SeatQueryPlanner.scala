package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.SeatQueryResponse
import Common.{SeatInfo, SeatPosition, SeatStatus}

case class SeatQueryPlanner(position: SeatPosition, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("Int", position.floor.toString),
        SqlParameter("Int", position.section.toString),
        SqlParameter("Int", position.seatNumber.toString)
      )
    ).map {
      case Nil =>
        SeatQueryResponse(None).asJson.noSpaces
      case rows =>
        rows.headOption.map { row =>
          SeatInfo(
            row.hcursor.get[Int]("floor").getOrElse(0),
            row.hcursor.get[Int]("section").getOrElse(0),
            row.hcursor.get[Int]("seat_number").getOrElse(0),
            SeatStatus.withName(row.hcursor.get[String]("status").getOrElse("Normal")),
            row.hcursor.get[String]("feedback").getOrElse(""),
            row.hcursor.get[Boolean]("occupied").getOrElse(false),
            row.hcursor.get[String]("student_number").getOrElse("")
          )
        }.map(seat => SeatQueryResponse(Some(seat)).asJson.noSpaces).getOrElse(SeatQueryResponse(None).asJson.noSpaces)
    }
  }
}
