package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{AddSeatMessage, AddSeatResponse}
import Common.SeatPosition

case class AddSeatPlanner(message: AddSeatMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 添加座位信息
    val position = message.position
    writeDB(
      s"INSERT INTO $schemaName.seats (floor, section, seat_number, status, feedback, occupied, student_number) VALUES (?, ?, ?, 'Normal', '', FALSE, '')",
      List(
        SqlParameter("Int", position.floor.toString),
        SqlParameter("Int", position.section.toString),
        SqlParameter("Int", position.seatNumber.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        AddSeatResponse(success = true, "Seat added successfully").asJson.noSpaces
      } else {
        AddSeatResponse(success = false, "Failed to add seat").asJson.noSpaces
      }
    }
  }
}
