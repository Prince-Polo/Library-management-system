package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{SeatConfirmResponse}

// 添加缺失的导入包
import io.circe.Encoder
import io.circe.generic.auto._
import Common.DBAPI.WriteDBMessage

case class SeatConfirmPlanner(
                               floor: String,
                               section: String,
                               seatNumber: String,
                               feedback: String,
                               override val planContext: PlanContext
                             ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.seats SET status = ?, feedback = ? WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("String", "Confirmed"),
        SqlParameter("String", feedback),
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected!="") {
        SeatConfirmResponse(success = true, s"Seat at position $floor-$section-$seatNumber confirmed successfully with feedback: $feedback").asJson.noSpaces
      } else {
        SeatConfirmResponse(success = false, s"Failed to confirm seat at position $floor-$section-$seatNumber").asJson.noSpaces
      }
    }
  }
}
