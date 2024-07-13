package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{SeatRefreshResponse}

// 添加缺失的导入包
import io.circe.Encoder
import io.circe.generic.auto._
import Common.DBAPI.WriteDBMessage

case class SeatRefreshPlanner(
                               floor: String,
                               section: String,
                               seatNumber: String,
                               override val planContext: PlanContext
                             ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.seats SET status = ?, feedback = ? WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("String", "Normal"),
        SqlParameter("String", ""),
        SqlParameter("String", floor),
        SqlParameter("String", section),
        SqlParameter("String", seatNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        SeatRefreshResponse(success = true, s"Seat at position $floor-$section-$seatNumber refreshed successfully").asJson.noSpaces
      } else {
        SeatRefreshResponse(success = false, s"Failed to refresh seat at position $floor-$section-$seatNumber").asJson.noSpaces
      }
    }
  }
}
