package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName


case class CheckStudentTaskStatusMessage(studentId: String)

case class CheckStudentTaskStatusPlanner(studentId: String, override val planContext: PlanContext) extends Planner[Boolean] {
  override def plan(using planContext: PlanContext): IO[Boolean] = {
    readDBRows(
      s"SELECT COUNT(*) as count FROM ${schemaName}.tasks WHERE studentid = ? AND status = 1",
      List(
        SqlParameter("String", studentId)
      )
    ).map { rows =>
      rows.head.hcursor.get[Int]("count").getOrElse(0) > 0
    }
  }
}
