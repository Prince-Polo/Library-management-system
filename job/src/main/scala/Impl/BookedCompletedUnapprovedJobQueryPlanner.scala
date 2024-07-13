/*
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{JobInfo, BookedCompletedUnapprovedJobQueryResponse, BookedCompletedUnapprovedJobQueryMessage}

//已预约已完成未被批准的任务查询
case class BookedCompletedUnapprovedJobQueryPlanner(message: BookedCompletedUnapprovedJobQueryMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT jobid, jobstudentid, jobshortdescription, joblongdescription, jobhardness, jobcredit, jobcomplete, jobbooked, jobapproved FROM ${schemaName}.jobs ORDER BY jobid",
      List()
    ).flatMap { rows =>
      val jobs = rows.map { row =>
        JobInfo(
          row.hcursor.get[Int]("jobid").getOrElse(0),
          row.hcursor.get[String]("jobshortdescription").getOrElse(""),
          row.hcursor.get[String]("joblongdescription").getOrElse(""),
          row.hcursor.get[String]("jobhardness").getOrElse(""),
          row.hcursor.get[String]("jobcredit").getOrElse("")
        )
      }.toList
      BookedCompletedUnapprovedJobQueryResponse(jobs).asJson.noSpaces
    }
  }
}
*/
