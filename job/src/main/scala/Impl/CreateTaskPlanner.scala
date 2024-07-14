package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{AddJobMessage, AddJobResponse}


case class CreateTaskMessage(jobId: Int, studentId: String)

case class CreateTaskPlanner(jobId: Int, studentId: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"INSERT INTO $schemaName.tasks (taskId, studentId, status) VALUES (?, ?, ?)",
      List(
        SqlParameter("Int", jobId.toString),
        SqlParameter("String",studentId),
        SqlParameter("Int", "0") // 初始状态为申请
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        "Task created successfully".asJson.noSpaces
      } else {
        "Failed to create task".asJson.noSpaces
      }
    }
  }
}
