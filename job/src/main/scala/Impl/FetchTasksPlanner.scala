package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI._
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI._

case class FetchTasksPlanner(studentId: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT t.taskId, t.status
         |FROM ${schemaName}.tasks t
         |JOIN ${schemaName}.jobs j ON t.taskid = j.jobId
         |WHERE t.studentid = ?
      """.stripMargin,
      List(SqlParameter("String", studentId))
    ).flatMap { rows =>
      val tasks = rows.map { row =>
        TaskInfo(
          row.hcursor.get[Int]("taskid").getOrElse(0),
          row.hcursor.get[Int]("status").getOrElse(0)
        )
      }.toList
      val response = FetchTasksResponse(tasks)
      IO.pure(response.asJson.noSpaces)
    }
  }
}
