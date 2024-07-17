package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{StudentInfoUsingTokenMessage, StudentInfoUsingTokenResponse}
import Utils.JWTUtil

case class StudentInfoUsingTokenPlanner(token: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // Validate token and retrieve student number
      studentNumberOpt <- IO(JWTUtil.getNumber(token))
      studentNumber <- studentNumberOpt match {
        case Some(number) => IO.pure(number)
        case None => IO.raiseError(new Exception("Invalid token"))
      }

      // Retrieve student info from the database
      rows <- readDBRows(
        s"""
           |SELECT user_name, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours
           |FROM $schemaName.students
           |WHERE number = ?
           |""".stripMargin,
        List(SqlParameter("String", studentNumber))
      )

      response <- rows.headOption match {
        case Some(row) =>
          val userName = row.hcursor.get[String]("userName").getOrElse("")
          val volunteerStatus = row.hcursor.get[String]("volunteerStatus").getOrElse("false")
          val floor = row.hcursor.get[String]("floor").getOrElse("0")
          val sectionNumber = row.hcursor.get[String]("sectionNumber").getOrElse("0")
          val seatNumber = row.hcursor.get[String]("seatNumber").getOrElse("0")
          val violationCount = row.hcursor.get[String]("violationCount").getOrElse("0")
          val volunteerHours = row.hcursor.get[String]("volunteerHours").getOrElse("0")

          IO.pure(
            StudentInfoUsingTokenResponse(
              userName,
              volunteerStatus,
              floor,
              sectionNumber,
              seatNumber,
              violationCount,
              volunteerHours
            ).asJson.noSpaces
          )
        case None =>
          IO.pure(s"""{"error": "Student not found"}""")
      }
    } yield response
  }
}
