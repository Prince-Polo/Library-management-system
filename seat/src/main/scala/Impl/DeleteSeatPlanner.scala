package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Common.SeatPosition

case class DeleteSeatPlanner(position: SeatPosition, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"DELETE FROM $schemaName.seats WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("Int", position.floor.toString),
        SqlParameter("Int", position.section.toString),
        SqlParameter("Int", position.seatNumber.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        s"Seat deleted successfully at position: ${position.floor}-${position.section}-${position.seatNumber}"
      } else {
        s"Failed to delete seat at position: ${position.floor}-${position.section}-${position.seatNumber}"
      }
    }
  }
}
