package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{DeleteSeatMessage, DeleteSeatResponse}
import Common.SeatPosition

case class DeleteSeatPlanner(message: DeleteSeatMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 删除座位信息
    val position = message.position
    writeDB(
      s"DELETE FROM $schemaName.seats WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("Int", position.floor.toString),
        SqlParameter("Int", position.section.toString),
        SqlParameter("Int", position.seatNumber.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        DeleteSeatResponse(success = true, "Seat deleted successfully").asJson.noSpaces
      } else {
        DeleteSeatResponse(success = false, "Failed to delete seat").asJson.noSpaces
      }
    }
  }
}
