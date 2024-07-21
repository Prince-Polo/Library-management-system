package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentUpdateResponse

case class StudentUpdatePlanner(userName: String, password: String, number: String, newPassword: Option[String], override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? AND password = ? AND number = ?)", List(SqlParameter("String", userName), SqlParameter("String", password), SqlParameter("String", number)))
      .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Student not found")))
      .flatMap(_ =>
        writeDB(
          s"UPDATE $schemaName.students SET ${newPassword.map(_ => "password = ?").getOrElse("")} WHERE user_name = ? AND password = ? AND number = ?",
          newPassword.map(SqlParameter("String", _) :: Nil).getOrElse(Nil) ++
            List(SqlParameter("String", userName), SqlParameter("String", password), SqlParameter("String", number))
        ).as(StudentUpdateResponse(success = true, message = s"Update successful for student with number: $number").asJson.noSpaces)
      ).handleError(error => StudentUpdateResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
