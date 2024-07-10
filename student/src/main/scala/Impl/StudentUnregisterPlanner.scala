package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.StudentAPI.StudentUnregisterMessage

case class StudentUnregisterPlanner(message: StudentUnregisterMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // Check if the student exists
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)",
      List(SqlParameter("String", message.number))
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        // Delete the student from the database
        writeDB(
          s"DELETE FROM $schemaName.students WHERE number = ?",
          List(SqlParameter("String", message.number))
        ).map { _ =>
          s"Unregistration successful for student with number: ${message.number}"
        }
      }
    }
  }
}
