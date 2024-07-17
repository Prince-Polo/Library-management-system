package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.*

case class FetchTasksPlanner(studentId: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT taskId, studentId, status FROM ${schemaName}.tasks WHERE studentId = ?",
      List(SqlParameter("String", studentId))
    ).flatMap { rows =>
      val tasks = rows.map { row =>
        TaskInfo(
          row.hcursor.get[Int]("taskid").getOrElse(0),
          row.hcursor.get[String]("studentid").getOrElse(""),
          row.hcursor.get[Int]("status").getOrElse(0)
        )
      }.toList
      IO.pure(tasks.asJson.noSpaces)
    }
  }
}
