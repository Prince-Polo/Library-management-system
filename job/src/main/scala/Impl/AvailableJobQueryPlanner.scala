package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{JobInfo, AvailableJobQueryResponse, AvailableJobQueryMessage}

case class AvailableJobQueryPlanner(message: AvailableJobQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT jobId, jobStudentId, jobShortDescription, jobLongDescription, jobHardness, jobCredit, jobComplete, jobBooked, jobApproved FROM ${schemaName}.jobs WHERE jobBooked = 'FALSE' ORDER BY jobId",
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
      AvailableJobQueryResponse(jobs).asJson.noSpaces
    }
  }
}
