// Impl.ReportedSeatQueryPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.ReportedSeatQueryResponse
import Common.{SeatInfo, SeatStatus}

case class ReportedSeatQueryPlanner(override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE status != 'Available' ORDER BY floor, section, seat_number",
      List()
    ).map(rows =>
      ReportedSeatQueryResponse(
        rows.map(row =>
          SeatInfo(
            floor = row.hcursor.get[String]("floor").getOrElse(""),
            section = row.hcursor.get[String]("section").getOrElse(""),
            seatNumber = row.hcursor.get[String]("seat_number").getOrElse(""),
            status = row.hcursor.get[String]("status").flatMap(SeatStatus.fromString).getOrElse(SeatStatus.Available),
            feedback = row.hcursor.get[String]("feedback").getOrElse(""),
            occupied = row.hcursor.get[Boolean]("occupied").getOrElse(false),
            studentNumber = row.hcursor.get[String]("student_number").getOrElse("")
          )
        ).toList
      ).asJson.noSpaces
    ).handleError(error => ReportedSeatQueryResponse(List()).asJson.noSpaces)
}
