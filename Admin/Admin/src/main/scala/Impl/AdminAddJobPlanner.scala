package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{AddJobMessage, AddJobResponse}


// Case class for adding a job with a planner context
case class AdminAddJobPlanner(message: AddJobMessage, override val planContext: PlanContext) extends Planner[String] {
  // Implementing the plan method using implicit PlanContext
  override def plan(using planContext: PlanContext): IO[String] = {
    // Inserting job information into the database
    writeDB(
      s"INSERT INTO $schemaName.jobs (job_id, job_student_id, job_short_description, job_long_description, job_hardness, job_credit, job_complete, job_booked, job_approved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
      List(
        SqlParameter("BigInt", message.jobId.toString),
        SqlParameter("String", message.jobStudentId),
        SqlParameter("String", message.jobShortDescription),
        SqlParameter("String", message.jobLongDescription),
        SqlParameter("BigInt", message.jobHardness.toString),
        SqlParameter("BigInt", message.jobCredit.toString),
        SqlParameter("Boolean", message.jobComplete.toString),
        SqlParameter("Boolean", message.jobBooked.toString),
        SqlParameter("Boolean", message.jobApproved.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        AddJobResponse(success = true, "Job added successfully").asJson.noSpaces
      } else {
        AddJobResponse(success = false, "Failed to add job").asJson.noSpaces
      }
    }
  }

  // Method to return the full plan execution
  override def fullPlan: IO[String] = plan(using planContext)
}
