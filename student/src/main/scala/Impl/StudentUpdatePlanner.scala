package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.StudentAPI.StudentUpdateMessage

case class StudentUpdatePlanner(message: StudentUpdateMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using PlanContext): IO[String] = {
    // Check if the student exists
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? AND password = ? AND email = ? AND number = ?)",
      List(
        SqlParameter("String", message.userName),
        SqlParameter("String", message.password),
        SqlParameter("String", message.email),
        SqlParameter("String", message.number)
      )
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        // Prepare the update statement
        val updateFields = List(
          message.newPassword.map(_ => "password = ?"),
          message.newEmail.map(_ => "email = ?")
        ).flatten.mkString(", ")

        val updateParams = List(
          message.newPassword.map(SqlParameter("String", _)),
          message.newEmail.map(SqlParameter("String", _))
        ).flatten ++ List(
          SqlParameter("String", message.userName),
          SqlParameter("String", message.password),
          SqlParameter("String", message.email),
          SqlParameter("String", message.number)
        )

        // Update the student in the database
        writeDB(
          s"UPDATE $schemaName.students SET $updateFields WHERE user_name = ? AND password = ? AND email = ? AND number = ?",
          updateParams
        ).map { _ =>
          s"Update successful for student with number: ${message.number}"
        }
      }
    }
  }
}
