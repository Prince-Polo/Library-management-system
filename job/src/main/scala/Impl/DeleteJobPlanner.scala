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
    writeDB(
      s"DELETE FROM $schemaName.jobs WHERE jobid = ?",
      List(
        SqlParameter("Int", jobId.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        DeleteJobResponse(success = true, "Job deleted successfully").asJson.noSpaces
      } else {
        DeleteJobResponse(success = false, "Failed to delete job").asJson.noSpaces
      }
    }
  }
}
