/*
package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class PatientRegisterMessagePlanner(userName: String, password: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using planContext: PlanContext): IO[String] = {
    // Check if the user is already registered
    val checkUserExists = readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.user_name WHERE user_name = ?)",
        List(SqlParameter("String", userName))
      )

    checkUserExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("already registered"))
      } else {
        writeDB(s"INSERT INTO ${schemaName}.user_name (user_name, password) VALUES (?, ?)",
          List(SqlParameter("String", userName),
               SqlParameter("String", password)
          ))
      }
    }
  }
*/
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
      List(SqlParameter("String", message.userName), SqlParameter("String", message.email), SqlParameter("String", message.number)))
    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        // Insert new student into the database
        writeDB(
          s"INSERT INTO ${schemaName}.students (user_name, password, email,number) VALUES (?, ?, ?,?)",
          List(SqlParameter("String", message.userName),
            SqlParameter("String", message.password),
            SqlParameter("String", message.email),
            SqlParameter("String", message.number))
        ).map(_ => "Registration successful")
      }
    }
  }
}

