package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{OccupiedSeatQueryMessage, OccupiedSeatQueryResponse}
import Common.SeatInfo
import Common.SeatStatus
import Common.SeatStatus.SeatStatus // 导入 SeatStatus

case class OccupiedSeatQueryPlanner(message: OccupiedSeatQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 查询被占用的座位信息
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE occupied = TRUE ORDER BY floor, section, seat_number",
      List() // 无需参数
    ).map { rows =>
      val seats = rows.map { row =>
        SeatInfo(
          row.hcursor.get[Int]("floor").getOrElse(0),
          row.hcursor.get[Int]("section").getOrElse(0),
          row.hcursor.get[Int]("seat_number").getOrElse(0),
          row.hcursor.get[SeatStatus]("status").getOrElse(SeatStatus.Normal),
          row.hcursor.get[String]("feedback").getOrElse(""),
          row.hcursor.get[Boolean]("occupied").getOrElse(true),
          row.hcursor.get[String]("student_number").getOrElse("")
        )
      }.toList
      OccupiedSeatQueryResponse(seats).asJson.noSpaces
    }
  }
}
