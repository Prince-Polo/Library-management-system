package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean, startTransaction}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentUnregisterResponse

case class StudentUnregisterPlanner(number: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)", List(SqlParameter("String", number)))
      .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Student not found")))
      .flatMap(_ =>
        startTransaction(
          writeDB(s"DELETE FROM $schemaName.students WHERE number = ?", List(SqlParameter("String", number))) *>
            writeDB(s"DELETE FROM $schemaName.student_tokens WHERE student_id = ?", List(SqlParameter("String", number)))
        ).as(StudentUnregisterResponse(success = true, message = s"Unregistration successful for student with number: $number").asJson.noSpaces)
      ).handleError(error => StudentUnregisterResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
