package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.Json
import io.circe.parser.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, readDBString}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

// 定义一个 case class 来表示登录响应
case class StudentLoginResponse(valid: Boolean, id: Option[Int] = None, authority: Option[Int] = None)

case class StudentLoginMessagePlanner(userName: String, password: String, email: String, number: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // 尝试通过从数据库读取行来验证患者
    readDBRows(
      s"SELECT id, authority FROM \"${schemaName}\".patient_info WHERE username = ? AND password = ? AND email = ? AND number = ?",
      List(
        SqlParameter("String", userName),
        SqlParameter("String", password),
        SqlParameter("String", email),
        SqlParameter("String", number)
      )
    ).map {
      case Nil =>
        // 用户无效，返回 JSON 字符串
        StudentLoginResponse(valid = false).asJson.noSpaces
      case rows =>
        // 用户有效，提取 id 和 authority 并返回 JSON 字符串
        rows.headOption match {
          case Some(row) =>
            // 解析 JSON 对象
            val id = row.hcursor.get[Int]("id").getOrElse(-1)
            val authority = row.hcursor.get[Int]("authority").getOrElse(-1)
            StudentLoginResponse(valid = true, Some(id), Some(authority)).asJson.noSpaces
          case None =>
            StudentLoginResponse(valid = false).asJson.noSpaces
        }
    }
  }
