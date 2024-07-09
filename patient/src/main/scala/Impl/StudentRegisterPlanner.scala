package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.PatientAPI.StudentRegisterMessage

case class StudentRegisterPlanner(message: StudentRegisterMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using PlanContext): IO[String] = {
    // Check if the student is already registered
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? OR email = ? OR number = ?)",
      List(
        SqlParameter("String", message.userName),
        SqlParameter("String", message.email),
        SqlParameter("String", message.number)
      )
    )

    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        // Insert new student into the database
        writeDB(
          s"INSERT INTO $schemaName.students (user_name, password, email, number) VALUES (?, ?, ?, ?)",
          List(
            SqlParameter("String", message.userName),
            SqlParameter("String", message.password),
            SqlParameter("String", message.email),
            SqlParameter("String", message.number)
          )
        ).map(_ => "Registration successful")
      }
    }
  }
}
