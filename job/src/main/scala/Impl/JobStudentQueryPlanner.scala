package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.*

case class JobStudentsQueryPlanner(jobId: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT studentid, status FROM ${schemaName}.tasks WHERE taskid = ?",
      List(SqlParameter("Int", jobId.toString))
    ).flatMap { rows =>
      val students = rows.map { row =>
        StudentInfo(
          row.hcursor.get[String]("studentid").getOrElse(""),
          row.hcursor.get[Int]("status").getOrElse(0)
        )
      }.toList
      IO.pure(students.asJson.noSpaces)
    }
  }
}
