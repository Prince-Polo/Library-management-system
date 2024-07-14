package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class DeleteSeatPlanner(
                              floor: String,
                              section: String,
                              seatNumber: String,
                              override val planContext: PlanContext
                            ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"DELETE FROM $schemaName.seats WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected != "") {
        s"Seat deleted successfully at position: $floor-$section-$seatNumber"
      } else {
        s"Failed to delete seat at position: $floor-$section-$seatNumber"
      }
    }
  }
}
