// Impl.QuerySeatsInSectionPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.QuerySeatsInSectionResponse
import Common.SeatInfo

case class QuerySeatsInSectionPlanner(floor: String, section: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT floor, section, seat_number, status, feedback, occupied, student_number
         |FROM $schemaName.seats
         |WHERE floor = ? AND section = ?
         |ORDER BY seat_number
       """.stripMargin,
      List(
        SqlParameter("String", floor),
        SqlParameter("String", section)
      )
    ).map { rows =>
      val seats = rows.map { row =>
        SeatInfo(
          floor = row.hcursor.get[String]("floor").getOrElse(""),
          section = row.hcursor.get[String]("section").getOrElse(""),
          seatNumber = row.hcursor.get[String]("seatNumber").getOrElse(""),
          status = row.hcursor.get[String]("status").getOrElse(""),
          feedback = row.hcursor.get[String]("feedback").getOrElse(""),
          occupied = row.hcursor.get[String]("occupied").getOrElse(""),
          studentNumber = row.hcursor.get[String]("studentNumber").getOrElse("")
        )
      }.toList
      val totalSeats = seats.size.toString
      val freeSeats = seats.count(_.occupied == "false").toString
      QuerySeatsInSectionResponse(totalSeats, freeSeats, seats).asJson.noSpaces
    }
  }
}
