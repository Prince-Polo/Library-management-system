package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.UpdateVolunteerHourResponse

case class UpdateVolunteerHourPlanner(jobId: Int, studentId: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBRows(s"SELECT jobcredit, jobhardness FROM job.jobs WHERE jobid = ?", List(SqlParameter("Int", jobId.toString)))
      .map(rows => (rows.head.hcursor.get[Int]("jobcredit").getOrElse(0), rows.head.hcursor.get[Int]("jobhardness").getOrElse(0)))
      .flatMap((credit, hardness) =>
        readDBRows(s"SELECT volunteer_hours FROM student.students WHERE number = ?", List(SqlParameter("String", studentId)))
          .map(_.head.hcursor.get[Float]("volunteer_hours").getOrElse(0.0f))
          .flatMap(currentVolunteerHour =>
            writeDB(s"UPDATE student.students SET volunteer_hours = ? WHERE number = ?",
              List(SqlParameter("Float", BigDecimal(currentVolunteerHour + credit * (1 + 0.1 * hardness)).setScale(1, BigDecimal.RoundingMode.HALF_UP).toFloat.toString), SqlParameter("String", studentId)))
              .map(rowsAffected => UpdateVolunteerHourResponse(success = rowsAffected.nonEmpty, message = if (rowsAffected.nonEmpty) s"Updated volunteer hours for student with number: $studentId" else s"Failed to update volunteer hours for student with number: $studentId").asJson.noSpaces)
          )
      ).handleError(error => UpdateVolunteerHourResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
