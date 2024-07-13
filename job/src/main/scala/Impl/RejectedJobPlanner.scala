package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{RejectedJobMessage, RejectedJobResponse}

case class RejectedJobPlanner(message: RejectedJobMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val updatedJobMessage = message.copy(
      jobStudentId = "",
      jobComplete = "FALSE",
      jobBooked = "FALSE",
      jobApproved = "FALSE"
    )

    writeDB(
      s"""
         |UPDATE $schemaName.jobs
         |SET jobStudentId = ?, jobShortDescription = ?, jobLongDescription = ?, jobHardness = ?, jobCredit = ?, jobComplete = ?, jobBooked = ?, jobApproved = ?
         |WHERE jobId = ?
         """.stripMargin,
      List(
        SqlParameter("String", updatedJobMessage.jobStudentId),
        SqlParameter("String", updatedJobMessage.jobShortDescription),
        SqlParameter("String", updatedJobMessage.jobLongDescription),
        SqlParameter("String", updatedJobMessage.jobHardness),
        SqlParameter("String", updatedJobMessage.jobCredit),
        SqlParameter("String", updatedJobMessage.jobComplete),
        SqlParameter("String", updatedJobMessage.jobBooked),
        SqlParameter("String", updatedJobMessage.jobApproved),
        SqlParameter("BigInt", updatedJobMessage.jobId.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        RejectedJobResponse(success = true, "Job rejected successfully").asJson.noSpaces
      } else {
        RejectedJobResponse(success = false, "Failed to reject job").asJson.noSpaces
      }
    }
  }
}
