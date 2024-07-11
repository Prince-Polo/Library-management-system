package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Common.BasicInfo

case class StudentRegisterPlanner(info: BasicInfo, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using PlanContext): IO[String] = {
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? OR number = ?)",
      List(SqlParameter("String", info.userName), SqlParameter("String", info.number))
    )
    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        writeDB(
          s"INSERT INTO ${schemaName}.students (user_name, password, number) VALUES (?, ?, ?)",
          List(SqlParameter("String", info.userName),
            SqlParameter("String", info.password),
            SqlParameter("String", info.number))
        ).map(_ => "Registration successful")
      }
    }
  }
}
