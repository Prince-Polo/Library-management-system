package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{SeatQueryMessage, SeatQueryResponse}
import Common.SeatInfo
import Common.SeatStatus
import Common.SeatStatus.SeatStatus // 导入 SeatStatus

case class SeatQueryPlanner(message: SeatQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 查询座位信息
    readDBRows(
      s"SELECT floor, section, seat_number, status, feedback, occupied, student_number FROM $schemaName.seats WHERE floor = ? AND section = ? AND seat_number = ?",
      List(
        SqlParameter("Int", message.floor.toString),
        SqlParameter("Int", message.section.toString),
        SqlParameter("Int", message.seatNumber.toString)
      )
    ).map {
      case Nil =>
        SeatQueryResponse(None).asJson.noSpaces
      case rows =>
        rows.headOption.map { row =>
          SeatInfo(
            row.hcursor.get[Int]("floor").getOrElse(0),
            row.hcursor.get[Int]("section").getOrElse(0),
            row.hcursor.get[Int]("seat_number").getOrElse(0),
            row.hcursor.get[SeatStatus]("status").getOrElse(SeatStatus.Normal),
            row.hcursor.get[String]("feedback").getOrElse(""),
            row.hcursor.get[Boolean]("occupied").getOrElse(false),
            row.hcursor.get[String]("student_number").getOrElse("")
          )
        } match {
          case Some(seatInfo) => SeatQueryResponse(Some(seatInfo)).asJson.noSpaces
          case None => SeatQueryResponse(None).asJson.noSpaces
        }
    }
  }
}
