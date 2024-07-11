package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Common.SeatPosition

case class AddSeatPlanner(position: SeatPosition, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"INSERT INTO $schemaName.seats (floor, section, seat_number, status, feedback, occupied, student_number) VALUES (?, ?, ?, 'Normal', '', false, '')",
      List(
        SqlParameter("Int", position.floor.toString),
        SqlParameter("Int", position.section.toString),
        SqlParameter("Int", position.seatNumber.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        s"Seat added successfully at position: ${position.floor}-${position.section}-${position.seatNumber}"
      } else {
        s"Failed to add seat at position: ${position.floor}-${position.section}-${position.seatNumber}"
      }
    }
  }
}
