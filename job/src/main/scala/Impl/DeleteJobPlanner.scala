package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{DeleteJobMessage, DeleteJobResponse}

// 管理员删除任务
case class DeleteJobPlanner(jobId: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // 将 jobs 表中的 jobComplete 设置为 true
      jobsUpdated <- writeDB(
        s"UPDATE $schemaName.jobs SET jobComplete = ? WHERE jobId = ?",
        List(
          SqlParameter("Boolean", "true"),
          SqlParameter("Int", jobId.toString)
        )
      )

      response = if (jobsUpdated.toInt > 0) {
        DeleteJobResponse(success = true, "Job marked as complete successfully").asJson.noSpaces
      } else {
        DeleteJobResponse(success = false, "Failed to mark job as complete").asJson.noSpaces
      }
    } yield response
  }
}
