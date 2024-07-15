package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{UpdateTaskStatusMessage, UpdateTaskStatusResponse}

case class UpdateTaskStatusPlanner(jobId: Int, studentId: Int, status: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      _ <- writeDB(
        s"UPDATE $schemaName.tasks SET status = ? WHERE taskid = ? AND studentid = ?",
        List(
          SqlParameter("Int", status.toString),
          SqlParameter("Int", jobId.toString),
          SqlParameter("String", studentId.toString)
        )
      )
      _ <- if (status == 1) {
        for {
          _ <- writeDB(
            s"UPDATE $schemaName.jobs SET jobcurrent = jobcurrent - 1, jobenrolled = jobenrolled + 1, jobrequired = jobrequired - 1 WHERE jobid = ?",
            List(SqlParameter("Int", jobId.toString))
          )
        } yield ()
      } else {
        for {
          _ <- writeDB(
            s"UPDATE $schemaName.jobs SET jobcurrent = jobcurrent - 1 WHERE jobid = ?",
            List(SqlParameter("Int", jobId.toString))
          )
        } yield ()
      }
    } yield {
      UpdateTaskStatusResponse(success = true, "Task status updated successfully").asJson.noSpaces
    }
  }
}
