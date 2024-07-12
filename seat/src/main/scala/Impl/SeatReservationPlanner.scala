package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{SeatReservationResponse}

case class SeatReservationPlanner(floor: String, section: String, seatNumber: String, studentNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.seats SET occupied = 'true', student_number = ? WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("String", studentNumber),
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        SeatReservationResponse(success = true, message = s"Seat status updated successfully at position: $floor-$section-$seatNumber").asJson.noSpaces
      } else {
        SeatReservationResponse(success = false, message = s"Failed to update seat status at position: $floor-$section-$seatNumber").asJson.noSpaces
      }
    }
  }
}
