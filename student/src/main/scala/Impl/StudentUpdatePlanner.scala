package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.StudentAPI.StudentUpdateMessage

case class StudentUpdatePlanner(message: StudentUpdateMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val messageInfo = message.info

    // 检查学生是否存在
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? AND password = ? AND number = ?)",
      List(
        SqlParameter("String", messageInfo.userName),
        SqlParameter("String", messageInfo.password),
        SqlParameter("String", messageInfo.number)
      )
    )

    checkStudentExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Student not found"))
      } else {
        // 更新密码
        val updateFields = List(
          message.newPassword.map(newPwd => s"password = ?")
        ).flatten.mkString(", ")

        val updateParams = List(
          message.newPassword.map(SqlParameter("String", _))
        ).flatten ++ List(
          SqlParameter("String", messageInfo.userName),
          SqlParameter("String", messageInfo.password),
          SqlParameter("String", messageInfo.number)
        )

        writeDB(
          s"UPDATE $schemaName.students SET $updateFields WHERE user_name = ? AND password = ? AND number = ?",
          updateParams
        ).map(_ => s"Update successful for student with number: ${messageInfo.number}")
      }
    }
  }
}
