/*
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{CompletedUnapprovedJobByStudentQueryMessage, CompletedUnapprovedJobByStudentQueryResponse, JobInfo}

case class CompletedUnapprovedJobByStudentQueryPlanner(message: CompletedUnapprovedJobByStudentQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT jobId, jobStudentId, jobShortDescription, jobLongDescription, jobHardness, jobCredit, jobComplete, jobBooked, jobApproved 
         |FROM $schemaName.jobs 
         |WHERE jobStudentId = '${message.studentId}' AND jobComplete = TRUE AND jobApproved = FALSE
         |ORDER BY jobId
         """.stripMargin,
      List()
    ).map { rows =>
      val jobs = rows.map { row =>
        JobInfo(
        /*  row.hcursor.get[String]("jobId").getOrElse(""),*/
          row.hcursor.get[String]("jobStudentId").getOrElse(""),
          row.hcursor.get[String]("jobShortDescription").getOrElse(""),
          row.hcursor.get[String]("jobLongDescription").getOrElse(""),
          row.hcursor.get[String]("jobHardness").getOrElse(""),
          row.hcursor.get[String]("jobCredit").getOrElse(""),
    /*      row.hcursor.get[Boolean]("jobComplete").getOrElse(false),
          row.hcursor.get[Boolean]("jobBooked").getOrElse(false),
          row.hcursor.get[Boolean]("jobApproved").getOrElse(false)*/
        )
      }.toList
      CompletedUnapprovedJobByStudentQueryResponse(jobs).asJson.noSpaces
    }
  }
}
*/
