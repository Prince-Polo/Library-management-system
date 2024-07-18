package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{ApproveJobMessage, ApproveJobResponse}

case class ApproveJobPlanner(message: ApproveJobMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val updatedJobMessage = message.copy(jobApproved = true)

    writeDB(
      s"""
         |UPDATE $schemaName.jobs
         |SET job_student_id = ?, job_short_description = ?, job_long_description = ?, job_hardness = ?, job_credit = ?, job_complete = ?, job_booked = ?, job_approved = ?, job_current = ?, job_required = ?
         |WHERE job_id = ?
         """.stripMargin,
      List(
        SqlParameter("StringArray", updatedJobMessage.jobStudentId.mkString("{", ",", "}")),
        SqlParameter("String", updatedJobMessage.jobShortDescription),
        SqlParameter("String", updatedJobMessage.jobLongDescription),
        SqlParameter("Int", updatedJobMessage.jobHardness.toString),
        SqlParameter("Int", updatedJobMessage.jobCredit.toString),
        SqlParameter("Boolean", updatedJobMessage.jobComplete.toString),
        SqlParameter("Boolean", updatedJobMessage.jobBooked.toString),
        SqlParameter("Boolean", updatedJobMessage.jobApproved.toString),
        SqlParameter("Int", updatedJobMessage.jobCurrent.toString),
        SqlParameter("Int", updatedJobMessage.jobRequired.toString),
        SqlParameter("BigInt", updatedJobMessage.jobId.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected!="") {
        ApproveJobResponse(success = true, "Job approved successfully").asJson.noSpaces
      } else {
        ApproveJobResponse(success = false, "Failed to approve job").asJson.noSpaces
      }
    }
  }
}
