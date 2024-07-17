package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean, startTransaction}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class StudentDeletePlanner(number: String, password: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ? AND password = ?)",
      List(SqlParameter("String", number), SqlParameter("String", password))
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Invalid number or password"))
      } else {
        startTransaction {
          for {
            // Delete student record
            _ <- writeDB(
              s"DELETE FROM $schemaName.students WHERE number = ? AND password = ?",
              List(SqlParameter("String", number), SqlParameter("String", password))
            )

            // Delete corresponding token
            _ <- writeDB(
              s"DELETE FROM $schemaName.student_tokens WHERE number = ?",
              List(SqlParameter("String", number))
            )

          } yield {
            s"Deletion successful for student with number: $number"
          }
        }
      }
    }
  }
}
