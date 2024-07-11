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
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE status = 'Reported' ORDER BY floor, section, seat_number",
      List()
    ).map { rows =>
      val seats = rows.map { row =>
        SeatInfo(
          row.hcursor.get[Int]("floor").getOrElse(0),
          row.hcursor.get[Int]("section").getOrElse(0),
          row.hcursor.get[Int]("seat_number").getOrElse(0),
          SeatStatus.withName(row.hcursor.get[String]("status").getOrElse("Normal")),
          row.hcursor.get[String]("feedback").getOrElse(""),
          row.hcursor.get[Boolean]("occupied").getOrElse(false),
          row.hcursor.get[String]("student_number").getOrElse("")
        )
      }.toList
      ReportedSeatQueryResponse(seats).asJson.noSpaces
    }
  }
}
