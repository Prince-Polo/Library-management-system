package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{AddSeatMessage, OccupiedSeatQueryResponse}
import Common.{SeatInfo, SeatPosition}


import org.slf4j.LoggerFactory


val logger = LoggerFactory.getLogger(getClass)

case class OccupiedSeatQueryPlanner(override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT floor, section, seat_number AS seatNumber, status, feedback, occupied, student_number AS studentNumber FROM $schemaName.seats WHERE status = 'Reported' ORDER BY floor, section, seat_number",
      List()
    ).map { rows =>
      val seats = rows.map { row =>
        SeatInfo(
          row.hcursor.get[String]("floor").getOrElse("0"),
          row.hcursor.get[String]("section").getOrElse("0"),
          row.hcursor.get[String]("seatNumber").getOrElse("0"),
          row.hcursor.get[String]("status").getOrElse("Normal"),
          row.hcursor.get[String]("feedback").getOrElse(""),
          row.hcursor.get[String]("occupied").getOrElse("false"),
          row.hcursor.get[String]("studentNumber").getOrElse("")
        )
      }.toList
      val response = OccupiedSeatQueryResponse(seats)
      logger.info(s"Returning response: ${response.asJson.noSpaces}")
      response.asJson.noSpaces
    }
  }
}
