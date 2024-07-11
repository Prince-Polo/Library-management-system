package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{AddJobMessage, AddJobResponse}
import io.circe.Encoder


//import APIs.JobAPI.AddJobResponse.encoder
//管理员发布任务
case class AddJobPlanner(
                                                   jobStudentId: String,
                                                   jobShortDescription: String,
                                                   jobLongDescription: String,
                                                   jobHardness: BigInt,
                                                   jobCredit: BigInt, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"INSERT INTO $schemaName.jobs (jobStudentId, jobShortDescription, jobLongDescription, jobHardness, jobCredit) VALUES (?, ?, ?, ?, ?)",
      List(
        //SqlParameter("BigInt", message.jobId.toString),
        SqlParameter("String", jobStudentId),
        SqlParameter("String", jobShortDescription),
        SqlParameter("String", jobLongDescription),
        SqlParameter("BigInt", jobHardness.toString),
        SqlParameter("BigInt", jobCredit.toString),

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
