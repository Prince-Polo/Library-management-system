package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{DeleteJobMessage, DeleteJobResponse}
import io.circe.KeyEncoder.encodeKeyInt

// 管理员删除任务
case class DeleteJobPlanner(jobId: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // 删除 tasks 表中的相关条目
      tasksDeleted <- writeDB(
        s"DELETE FROM $schemaName.tasks WHERE taskid = ?",
        List(
          SqlParameter("Int", jobId.toString)
        )
      )

      // 删除 jobs 表中的条目
      jobsDeleted <- writeDB(
        s"DELETE FROM $schemaName.jobs WHERE jobid = ?",
        List(
          SqlParameter("Int", jobId.toString)
        )
      )

      response = if (jobsDeleted.toInt > 0) {
        DeleteJobResponse(success = true, "Job deleted successfully").asJson.noSpaces
      } else {
        DeleteJobResponse(success = false, "Failed to delete job").asJson.noSpaces
      }
    } yield response
  }
}
