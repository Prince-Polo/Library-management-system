package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean, startTransaction}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentDeleteResponse
import Utils.JWTUtil

case class StudentDeletePlanner(token: String, password: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(number =>
        readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ? AND password = ?)", List(SqlParameter("String", number), SqlParameter("String", password)))
          .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Invalid number or password")) *>
            startTransaction(
              writeDB(s"DELETE FROM $schemaName.students WHERE number = ? AND password = ?", List(SqlParameter("String", number), SqlParameter("String", password))) *>
                writeDB(s"DELETE FROM $schemaName.student_tokens WHERE number = ?", List(SqlParameter("String", number)))
                  .as(StudentDeleteResponse(success = true, message = s"Deletion successful for student with number: $number").asJson.noSpaces)
            )
          )
      ).handleError(error => StudentDeleteResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
