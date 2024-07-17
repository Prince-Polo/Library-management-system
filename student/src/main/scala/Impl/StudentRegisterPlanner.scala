package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{StudentRegisterResponse}
import Utils.JWTUtil.createToken

case class StudentRegisterPlanner(
                                   userName: String,
                                   password: String,
                                   number: String,
                                   override val planContext: PlanContext
                                 ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? OR number = ?)",
      List(SqlParameter("String", userName), SqlParameter("String", number))
    )

    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        // Create a token for the student
        val token = createToken(number)

        writeDB(
          s"INSERT INTO ${schemaName}.students (user_name, password, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
          List(
            SqlParameter("String", userName),
            SqlParameter("String", password),
            SqlParameter("String", number),
            SqlParameter("String", "false"),  // initial volunteer status as string
            SqlParameter("String", "0"),  // initial floor as string
            SqlParameter("String", "0"),  // initial section_number as string
            SqlParameter("String", "0"),  // initial seat_number as string
            SqlParameter("String", "0"),  // initial violation_count as string
            SqlParameter("String", "0")  // initial volunteer_hours as string
          )
        ).flatMap { _ =>
          writeDB(
            s"INSERT INTO ${schemaName}.student_tokens (number, token) VALUES (?, ?)",
            List(
              SqlParameter("String", number),
              SqlParameter("String", token)
            )
          ).map { _ =>
            StudentRegisterResponse(
              userName = userName,
              volunteerStatus = "false",  // keeping boolean representation
              floor = "0",
              sectionNumber = "0",
              seatNumber = "0",
              violationCount = "0",
              volunteerHours = "0",
              token = token  // return the generated token
            ).asJson.noSpaces
          }
        }
      }
    }
  }
}
