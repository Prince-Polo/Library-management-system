package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{VolunteerStatusTrueResponse}

case class VolunteerStatusTruePlanner(number: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)",
      List(SqlParameter("String", number))
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        writeDB(
          s"UPDATE $schemaName.students SET volunteer_status = 'true' WHERE number = ?",
          List(SqlParameter("String", number))
        ).map { _ =>
          VolunteerStatusTrueResponse(success = true, message = s"Volunteer status updated to true for student with number: $number").asJson.noSpaces
        }
      }
    }.handleErrorWith { error =>
      IO.pure(VolunteerStatusTrueResponse(success = false, message = error.getMessage).asJson.noSpaces)
    }
  }
}
