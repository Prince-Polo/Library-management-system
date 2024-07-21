// Impl.ReleaseSeatPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.ReleaseSeatResponse

case class ReleaseSeatPlanner(floor: String, section: String, seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    writeDB(
      s"UPDATE $schemaName.seats SET occupied = false WHERE floor = ? AND section = ? AND seat_number = ?",
      List(SqlParameter("String", floor), SqlParameter("String", section), SqlParameter("String", seatNumber))
    ).map(result =>
      ReleaseSeatResponse(
        success = result.nonEmpty,
        message = if (result.nonEmpty) s"Seat $seatNumber in section $section on floor $floor is now unoccupied."
        else s"Failed to release seat $seatNumber in section $section on floor $floor."
      ).asJson.noSpaces
    ).handleError(error => ReleaseSeatResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
