package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class TeacherDeleteSeatPlanner(seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using PlanContext): IO[String] = {
    writeDB(
      s"DELETE FROM $schemaName.seats WHERE id = ?",
      List(SqlParameter("String", seatNumber), SqlParameter("String", "available"))
    ).map(_ => "Seat deleted successfully")
  }
}
