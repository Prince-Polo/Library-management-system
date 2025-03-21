package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class TeacherAddSeatPlanner(seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using PlanContext): IO[String] = {    /** 插入座位信息到数据库 */
    writeDB(
      s"INSERT INTO $schemaName.seats (seat_number, status) VALUES (?, ?)",
      List(SqlParameter("String", seatNumber), SqlParameter("String", "available"))
    ).map(_ => "Seat added successfully")
  }
}
