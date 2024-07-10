package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.StudentAPI.StudentRegisterMessage

case class StudentRegisterPlanner(message: StudentRegisterMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    val messageInfo = message.info
    // 检查学生是否已存在
    val checkStudentExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? OR number = ?)",
      List(
        SqlParameter("String", messageInfo.userName),
        SqlParameter("String", messageInfo.number)
      )
    )

    checkStudentExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Student already registered"))
      } else {
        // 插入新学生
        writeDB(
          s"""
             |INSERT INTO $schemaName.students (
             |  user_name, password, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours, completed_task_ids
             |) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
             |""".stripMargin,
          List(
            SqlParameter("String", messageInfo.userName),
            SqlParameter("String", messageInfo.password),
            SqlParameter("String", messageInfo.number),
            SqlParameter("Boolean", "false"), // 初始志愿者状态
            SqlParameter("Int", "0"),         // 初始楼层
            SqlParameter("Int", "0"),         // 初始区号
            SqlParameter("Int", "0"),         // 初始座位号
            SqlParameter("Int", "0"),         // 初始违约次数
            SqlParameter("Int", "0"),         // 初始志愿工时
            SqlParameter("Array", "[]")       // 初始已完成任务ID数组，作为空JSON数组字符串
          )
        ).map(_ => "Registration successful")
      }
    }
  }
}
