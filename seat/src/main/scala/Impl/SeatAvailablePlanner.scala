package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{SeatAvailableMessage, SeatAvailableResponse, SeatInfo}

case class SeatAvailablePlanner(message: SeatAvailableMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 查询可用且未被占用的座位信息
    readDBRows(
      s"SELECT floor, section, seat_number FROM $schemaName.seats WHERE occupied = FALSE ORDER BY floor, section, seat_number",
      List() // 无需参数
    ).map { rows =>
      val seats = rows.map { row =>
        SeatInfo(
          row.hcursor.get[Int]("floor").getOrElse(0),
          row.hcursor.get[Int]("section").getOrElse(0),
          row.hcursor.get[Int]("seat_number").getOrElse(0)
        )
      }.toList
      SeatAvailableResponse(seats).asJson.noSpaces
    }
  }
}
