package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.ServiceUtils.schemaName
import APIs.JobAPI.JobInfo

case class ApprovedJobQueryPlanner(override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"SELECT jobid, jobshortdescription, joblongdescription, jobhardness, jobcredit, jobcurrent, jobrequired, jobenrolled FROM ${schemaName}.jobs WHERE jobbooked = FALSE ORDER BY jobid",
      List()
    ).flatMap { rows =>
      val jobs = rows.map { row =>
        JobInfo(
          row.hcursor.get[Int]("jobid").getOrElse(0),
          row.hcursor.get[String]("jobshortdescription").getOrElse(""),
          row.hcursor.get[String]("joblongdescription").getOrElse(""),
          row.hcursor.get[Int]("jobhardness").getOrElse(0),
          row.hcursor.get[Int]("jobcredit").getOrElse(0),
          row.hcursor.get[Int]("jobcurrent").getOrElse(0),
          row.hcursor.get[Int]("jobrequired").getOrElse(0),
          row.hcursor.get[Int]("jobenrolled").getOrElse(0) // 新增字段
        )
      }.toList
      IO.pure(jobs.asJson.noSpaces)
    }
  }
}

