package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class ForceEndTaskPlanner(jobId: Int, studentId: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // Update the task status to 5 (forced end)
      _ <- writeDB(
        s"UPDATE $schemaName.tasks SET status = 5 WHERE taskid = ? AND studentid = ?",
        List(
          SqlParameter("Int", jobId.toString),
          SqlParameter("String", studentId)
        )
      )
      // Decrement the enrolled count for the job
      _ <- writeDB(
        s"UPDATE $schemaName.jobs SET jobEnrolled = jobEnrolled - 1 WHERE jobId = ?",
        List(
          SqlParameter("Int", jobId.toString)
        )
      )
    } yield {
      "Task forcibly ended and job enrolled count updated successfully".asJson.noSpaces
    }
  }
}
