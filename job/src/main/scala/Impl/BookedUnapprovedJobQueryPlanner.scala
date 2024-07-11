package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{JobInfo, BookedUnapprovedJobQueryResponse, BookedUnapprovedJobQueryMessage}

//已预约已完成未被批准的任务查询
case class BookedUnapprovedJobQueryPlanner(message: BookedUnapprovedJobQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT jobId, jobStudentId, jobShortDescription, jobLongDescription, jobHardness, jobCredit, jobComplete, jobBooked, jobApproved
         |FROM ${schemaName}.jobs
         |WHERE jobBooked = TRUE AND jobApproved = FALSE
         |ORDER BY jobId
         """.stripMargin,
      List()
    ).map { rows =>
      val jobs = rows.map { row =>
        JobInfo(
          row.hcursor.get[BigInt]("jobId").getOrElse(BigInt(0)),
          row.hcursor.get[String]("jobStudentId").getOrElse(""),
          row.hcursor.get[String]("jobShortDescription").getOrElse(""),
          row.hcursor.get[String]("jobLongDescription").getOrElse(""),
          row.hcursor.get[String]("jobHardness").getOrElse(""),
          row.hcursor.get[String]("jobCredit").getOrElse(""),
          row.hcursor.get[String]("jobComplete").getOrElse("FALSE"),
          row.hcursor.get[String]("jobBooked").getOrElse("FALSE"),
          row.hcursor.get[String]("jobApproved").getOrElse("FALSE")
        )
      }.toList
      BookedUnapprovedJobQueryResponse(jobs).asJson.noSpaces
    }
  }
}
