package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{AddSeatMessage, AddSeatResponse}

case class AddSeatPlanner(
                           floor: String,
                           section: String,
                           seatNumber: String,
                           override val planContext: PlanContext
                         ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"INSERT INTO $schemaName.seats (floor, section, seat_number, status, feedback, occupied, student_number) VALUES (?, ?, ?, 'available', '', false, '')",
      List(
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected != "") {
        AddSeatResponse(success = true, s"Seat added successfully at position: $floor-$section-$seatNumber").asJson.noSpaces
      } else {
        AddSeatResponse(success = false, s"Failed to add seat at position: $floor-$section-$seatNumber").asJson.noSpaces
      }
    }
  }
}
