package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.SeatQueryMessage

case class SeatQueryPlanner(message: SeatQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 查询座位信息
    readDBRows(
      s"SELECT * FROM $schemaName.seats WHERE section = ? AND seat_number = ?",
      List(
        SqlParameter("String", message.section),
        SqlParameter("String", message.seatNumber)
      )
    ).map {
      case Nil =>
        s"""{"error": "Seat not found"}"""
      case rows =>
        rows.headOption.map { row =>
          row.asJson.noSpaces
        }.getOrElse(s"""{"error": "Seat not found"}""")
    }
  }
}
