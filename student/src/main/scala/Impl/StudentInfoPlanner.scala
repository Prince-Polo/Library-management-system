package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.StudentAPI.{StudentInfoMessage, StudentInfoResponse}

case class StudentInfoPlanner(message: StudentInfoMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 查询学生信息
    readDBRows(
      s"SELECT user_name, email, number FROM $schemaName.students WHERE number = ?",
      List(SqlParameter("String", message.number))
    ).map {
      case Nil =>
        // 如果没有找到学生信息，返回错误信息
        s"""{"error": "Student not found"}"""
      case rows =>
        // 提取学生信息并返回 JSON 字符串
        rows.headOption match {
          case Some(row) =>
            val userName = row.hcursor.get[String]("user_name").getOrElse("")
            val email = row.hcursor.get[String]("email").getOrElse("")
            val number = row.hcursor.get[String]("number").getOrElse("")
            StudentInfoResponse(userName, email, number).asJson.noSpaces
          case None =>
            s"""{"error": "Student not found"}"""
        }
    }
  }
}
