package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.ReportedSeatQueryResponse
import Common.SeatInfo

case class ReportedSeatQueryPlanner(override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE status != 'available' ORDER BY floor, section, seat_number",
      List()
    ).map { rows =>
      val seats = rows.map { row =>
        SeatInfo(
          row.hcursor.get[String]("floor").getOrElse(""),
          row.hcursor.get[String]("section").getOrElse(""),
          row.hcursor.get[String]("seatNumber").getOrElse(""),
          row.hcursor.get[String]("status").getOrElse("Normal"),
          row.hcursor.get[String]("feedback").getOrElse(""),
          row.hcursor.get[String]("occupied").getOrElse("false"),
          row.hcursor.get[String]("studentNumber").getOrElse("")
        )
      }.toList
      ReportedSeatQueryResponse(seats).asJson.noSpaces
    }
  }
}
