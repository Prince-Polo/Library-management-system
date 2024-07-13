package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.JobAPI.{CreditJobMessage, CreditJobResponse}

case class CreditJobPlanner(message: CreditJobMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 首先检查 jobComplete, jobBooked 和 jobApproved 是否为 "TRUE"
    readDBRows(
      s"""
         |SELECT jobComplete, jobBooked, jobApproved
         |FROM $schemaName.jobs
         |WHERE jobId = ?
         """.stripMargin,
      List(SqlParameter("BigInt", message.jobId.toString))
    ).flatMap { rows =>
      val row = rows.headOption
      val jobComplete = row.flatMap(_.hcursor.get[String]("jobComplete").toOption).getOrElse("FALSE")
      val jobBooked = row.flatMap(_.hcursor.get[String]("jobBooked").toOption).getOrElse("FALSE")
      val jobApproved = row.flatMap(_.hcursor.get[String]("jobApproved").toOption).getOrElse("FALSE")

      if (jobComplete == "TRUE" && jobBooked == "TRUE" && jobApproved == "TRUE") {
        // 条件满足，执行更新操作
        writeDB(
          s"""
             |UPDATE $schemaName.jobs
             |SET jobStudentId = ?, jobShortDescription = ?, jobLongDescription = ?, jobHardness = ?, jobCredit = ?, jobComplete = ?, jobBooked = ?, jobApproved = ?
             |WHERE jobId = ?
             """.stripMargin,
          List(
            SqlParameter("String", message.jobStudentId),
            SqlParameter("String", message.jobShortDescription),
            SqlParameter("String", message.jobLongDescription),
            SqlParameter("String", message.jobHardness),
            SqlParameter("String", message.jobCredit),
            SqlParameter("String", message.jobComplete),
            SqlParameter("String", message.jobBooked),
            SqlParameter("String", message.jobApproved),
            SqlParameter("BigInt", message.jobId.toString)
          )
        ).map { rowsAffected =>
          if (rowsAffected.toInt > 0) {
            CreditJobResponse(success = true, "Job credit updated successfully").asJson.noSpaces
          } else {
            CreditJobResponse(success = false, "Failed to update job credit").asJson.noSpaces
          }
        }
      } else {
        // 条件不满足，返回错误响应
        IO.pure(CreditJobResponse(success = false, "Job is not complete, booked, or approved").asJson.noSpaces)
      }
    }
  }
}
