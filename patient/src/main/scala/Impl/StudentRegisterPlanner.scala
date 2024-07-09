package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class StudentRegisterPlanner(userName: String, password: String, email: String,number: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Check if the student is already registered
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.user_name WHERE user_name = ?)",
      List(SqlParameter("String", userName))
    )

    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        // Insert new student into the database
        writeDB(
          s"INSERT INTO $schemaName.user_name (user_name, password, email, number) VALUES (?, ?, ?, ?)",
          List(SqlParameter("String", userName), SqlParameter("String", password), SqlParameter("String", email),SqlParameter("String", number))
        ).map(_ => "Registration successful")
      }
    }
  }

