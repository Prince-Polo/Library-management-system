package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class StudentUpdatePlanner(userName: String, password: String, email: String, number: String, newPassword: Option[String] = None, newEmail: Option[String] = None, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // Check if the student exists
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? AND password = ? AND email = ? AND number = ?)",
      List(
        SqlParameter("String", userName),
        SqlParameter("String", password),
        SqlParameter("String", email),
        SqlParameter("String", number)
      )
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        // Prepare the update statement
        val updateFields = List(
          newPassword.map(_ => "password = ?"),
          newEmail.map(_ => "email = ?")
        ).flatten.mkString(", ")

        val updateParams = List(
          newPassword.map(SqlParameter("String", _)),
          newEmail.map(SqlParameter("String", _))
        ).flatten ++ List(
          SqlParameter("String", userName),
          SqlParameter("String", password),
          SqlParameter("String", email),
          SqlParameter("String", number)
        )

        // Update the student in the database
        writeDB(
          s"UPDATE $schemaName.students SET $updateFields WHERE user_name = ? AND password = ? AND email = ? AND number = ?",
          updateParams
        ).map { _ =>
          s"Update successful for student with number: $number"
        }
      }
    }
  }
}
