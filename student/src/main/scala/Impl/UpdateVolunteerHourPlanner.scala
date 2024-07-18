package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import org.slf4j.LoggerFactory

case class UpdateVolunteerHourPlanner(jobId: Int, studentId: String) extends Planner[String] {
  private val logger = LoggerFactory.getLogger(this.getClass)

  override def plan(using planContext: PlanContext): IO[String] = {
    val jobDetailsIO = readDBRows(
      s"SELECT jobcredit, jobhardness FROM job.jobs WHERE jobid = ?",
      List(SqlParameter("Int", jobId.toString))
    ).map { rows =>
      val job = rows.head
      val credit = job.hcursor.get[Int]("jobcredit").getOrElse(0)
      val hardness = job.hcursor.get[Int]("jobhardness").getOrElse(0)
      logger.info(s"Job Credit: $credit")
      logger.info(s"Job Hardness: $hardness")
      (credit, hardness)
    }

    val currentVolunteerHourIO = readDBRows(
      s"SELECT volunteer_hours FROM student.students WHERE number = ?",
      List(SqlParameter("String", studentId))
    ).map { rows =>
      val hours = rows.head.hcursor.get[String]("volunteerHours").getOrElse("0").toFloat
      logger.info(s"Current Volunteer Hours: $hours")
      hours
    }

    for {
      jobDetails <- jobDetailsIO
      currentVolunteerHour <- currentVolunteerHourIO
      (credit, hardness) = jobDetails
      volunteerHour = credit *(1+0.1* hardness)
    /*  newVolunteerHour = currentVolunteerHour + volunteerHour*/
      newVolunteerHour = BigDecimal(currentVolunteerHour + volunteerHour).setScale(1, BigDecimal.RoundingMode.HALF_UP).toFloat
      _ = logger.info(s"New Volunteer Hours: $newVolunteerHour")
      result <- writeDB(
        s"UPDATE student.students SET volunteer_hours = ? WHERE number = ?",
        List(
          SqlParameter("String", newVolunteerHour.toString),
          SqlParameter("String", studentId)
        )
      )
    } yield {
      if (result.nonEmpty) {
        s"Volunteer hours updated successfully for student $studentId".asJson.noSpaces
      } else {
        s"Failed to update volunteer hours for student $studentId".asJson.noSpaces
      }
    }
  }
}
