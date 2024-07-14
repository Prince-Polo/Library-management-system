package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{AddJobMessage, AddJobResponse}

// 管理员发布任务
case class AddJobPlanner(
                          JobShortDescription: String,
                          JobLongDescription: String,
                          JobHardness: Int,
                          JobCredit: Int,
                          JobRequired: Int,
                          override val planContext: PlanContext
                        ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"INSERT INTO $schemaName.jobs ( jobShortDescription, jobLongDescription, jobHardness, jobCredit, jobRequired, jobCurrent, jobEnrolled) VALUES (?, ?, ?, ?, ?, ?, ?)",
      List(
        SqlParameter("String", JobShortDescription),
        SqlParameter("String", JobLongDescription),
        SqlParameter("Int", JobHardness.toString),
        SqlParameter("Int", JobCredit.toString),
        SqlParameter("Int", JobRequired.toString),
        SqlParameter("Int", "0"), // 初始预约人数为 0
        SqlParameter("Int", "0") // 添加新的参数
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        AddJobResponse(success = true, "Job added successfully").asJson.noSpaces
      } else {
        AddJobResponse(success = false, "Failed to add job").asJson.noSpaces
      }
    }
  }
}

