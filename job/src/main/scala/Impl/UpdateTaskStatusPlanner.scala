package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class UpdateTaskStatusMessage(jobId: Int, studentId: String, status: Int)

case class UpdateTaskStatusPlanner(jobId: Int, studentId: String, status: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val updateStatusQuery =
      s"UPDATE $schemaName.tasks SET status = ? WHERE taskid = ? AND studentid = ?"

    val updateJobEnrolledQuery =
      if (status == 1) {
        s"UPDATE $schemaName.jobs SET jobEnrolled = jobEnrolled + 1, jobCurrent=jobCurrent-1, jobRequired=jobRequired-1 WHERE jobId = ?"
      } else if (status == 4 || status == 5) {
        s"UPDATE $schemaName.jobs SET jobEnrolled = jobEnrolled - 1 WHERE jobId = ?"
      }else if(status==2){
        s"UPDATE $schemaName.jobs SET jobCurrent = jobCurrent - 1 WHERE jobId = ?"
      }
      else {
        ""
      }

    val updateStatusIO = writeDB(
      updateStatusQuery,
      List(
        SqlParameter("Int", status.toString),
        SqlParameter("Int", jobId.toString),
        SqlParameter("String", studentId)
      )
    )

    val updateJobEnrolledIO = if (updateJobEnrolledQuery.nonEmpty) {
      writeDB(
        updateJobEnrolledQuery,
        List(
          SqlParameter("Int", jobId.toString)
        )
      )
    } else {
      IO.pure(0)
    }

    for {
      rowsAffected <- updateStatusIO
      _ <- updateJobEnrolledIO
    } yield {
      if (rowsAffected!="") {
        "Task status updated successfully".asJson.noSpaces
      } else {
        "Failed to update task status".asJson.noSpaces
      }
    }
  }
}
