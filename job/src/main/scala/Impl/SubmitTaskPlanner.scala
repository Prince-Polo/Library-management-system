package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{SubmitTaskMessage, SubmitTaskResponse}

case class SubmitTaskPlanner(jobId: Int, studentId: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE ${schemaName}.tasks SET status = 3 WHERE taskid = ? AND studentid = ?",
      List(
        SqlParameter("Int", jobId.toString),
        SqlParameter("String", studentId)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        SubmitTaskResponse(success = true, message = "Task submitted successfully").asJson.noSpaces
      } else {
        SubmitTaskResponse(success = false, message = "Failed to submit task").asJson.noSpaces
      }
    }.handleErrorWith { error =>
      IO.pure(SubmitTaskResponse(success = false, message = error.getMessage).asJson.noSpaces)
    }
  }
}
