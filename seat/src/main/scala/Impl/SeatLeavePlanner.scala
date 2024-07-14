package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{SeatLeaveResponse}

case class SeatLeavePlanner(
                             floor: String,
                             section: String,
                             seatNumber: String,
                             studentNumber: String,
                             override val planContext: PlanContext
                           ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.seats SET occupied = 'false', student_number = '' WHERE floor = ? AND section = ? AND seat_number = ? AND student_number = ?",
      List(
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber),
        SqlParameter("String", studentNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected != "") {
        SeatLeaveResponse(success = true, message = s"Seat status cleared successfully at position: $floor-$section-$seatNumber").asJson.noSpaces
      } else {
        SeatLeaveResponse(success = false, message = s"Failed to clear seat status at position: $floor-$section-$seatNumber").asJson.noSpaces
      }
    }
  }
}
