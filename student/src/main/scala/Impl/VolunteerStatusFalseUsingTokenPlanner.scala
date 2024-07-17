package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBBoolean, readDBRows, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{VolunteerStatusFalseUsingTokenResponse}
import Utils.JWTUtil

case class VolunteerStatusFalseUsingTokenPlanner(token: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // Validate token and retrieve student number
      studentNumberOpt <- IO(JWTUtil.getNumber(token))
      studentNumber <- studentNumberOpt match {
        case Some(number) => IO.pure(number)
        case None => IO.raiseError(new Exception("Invalid token"))
      }

      // Check if student exists
      checkStudentExists <- readDBBoolean(
        s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)",
        List(SqlParameter("String", studentNumber))
      )

      _ <- if (!checkStudentExists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        // Update volunteer status
        writeDB(
          s"UPDATE $schemaName.students SET volunteer_status = 'false' WHERE number = ?",
          List(SqlParameter("String", studentNumber))
        ).map { _ =>
          VolunteerStatusFalseUsingTokenResponse(success = true, message = s"Volunteer status updated to false for student with number: $studentNumber").asJson.noSpaces
        }
      }
    } yield VolunteerStatusFalseUsingTokenResponse(success = true, message = s"Volunteer status updated to false for student with number: $studentNumber").asJson.noSpaces
  }.handleErrorWith { error =>
    IO.pure(VolunteerStatusFalseUsingTokenResponse(success = false, message = error.getMessage).asJson.noSpaces)
  }
}
