package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBRows}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class StudentReservationPlanner(studentName: String, facilityName: String, reservationTime: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {    /** 插入预约信息到数据库 */
    writeDB(
      s"INSERT INTO $schemaName.reservations (student_name, facility_name, reservation_time) VALUES (?, ?, ?)",
      List(SqlParameter("String", studentName), SqlParameter("String", facilityName), SqlParameter("String", reservationTime))
    ).map(_ => "Reservation successful")
  }

