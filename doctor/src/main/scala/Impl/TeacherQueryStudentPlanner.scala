package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.Json
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.DoctorAPI.QueryParamMessage
import  APIs.PatientAPI.StudentInfoResponse

case class TeacherQueryStudentPlanner(message: QueryParamMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // 定义SQL查询和参数
    val sql = s"SELECT user_name, email, number FROM $schemaName.students WHERE user_name LIKE ? OR email LIKE ?"
    val params = List(SqlParameter("String", s"%${message.queryParam}%"), SqlParameter("String", s"%${message.queryParam}%"))

    // 使用readDBRows执行查询并处理结果
    readDBRows(sql, params).map {
      case Nil =>
        // 如果没有找到符合条件的学生信息，返回错误信息
        Json.obj("error" -> Json.fromString("No students found")).noSpaces
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