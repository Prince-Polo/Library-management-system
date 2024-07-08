package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class StudentRegisterPlanner(userName: String, password: String, email: String, number: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // 检查学生是否已经注册
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? OR email = ? OR number = ?)",
      List(
        SqlParameter("String", userName),
        SqlParameter("String", email),
        SqlParameter("String", number)
      )
    )

    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        // 将新学生插入数据库
        writeDB(
          s"INSERT INTO $schemaName.students (user_name, password, email, number) VALUES (?, ?, ?, ?)",
          List(
            SqlParameter("String", userName),
            SqlParameter("String", password),
            SqlParameter("String", email),
            SqlParameter("String", number)
          )
        ).map(_ => "Registration successful")
      }
    }
  }
