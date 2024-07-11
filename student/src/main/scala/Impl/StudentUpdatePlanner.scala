package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Common.BasicInfo

case class StudentUpdatePlanner(info: BasicInfo, newPassword: Option[String], override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? AND password = ? AND number = ?)",
      List(SqlParameter("String", info.userName), SqlParameter("String", info.password), SqlParameter("String", info.number))
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        val updateFields = List(
          newPassword.map(_ => "password = ?")
        ).flatten.mkString(", ")

        val updateParams = List(
          newPassword.map(SqlParameter("String", _))
        ).flatten ++ List(
          SqlParameter("String", info.userName),
          SqlParameter("String", info.password),
          SqlParameter("String", info.number)
        )

        writeDB(
          s"UPDATE $schemaName.students SET $updateFields WHERE user_name = ? AND password = ? AND number = ?",
          updateParams
        ).map { _ =>
          s"Update successful for student with number: ${info.number}"
        }
      }
    }
  }
}
