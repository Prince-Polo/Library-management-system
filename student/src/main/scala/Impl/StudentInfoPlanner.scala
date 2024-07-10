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
      s"""
         |SELECT user_name, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours, completed_task_ids
         |FROM $schemaName.students
         |WHERE number = ?
         |""".stripMargin,
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
            val number = row.hcursor.get[String]("number").getOrElse("")
            val volunteerStatus = row.hcursor.get[Boolean]("volunteer_status").getOrElse(false)
            val floor = row.hcursor.get[Int]("floor").getOrElse(0)
            val sectionNumber = row.hcursor.get[Int]("section_number").getOrElse(0)
            val seatNumber = row.hcursor.get[Int]("seat_number").getOrElse(0)
            val violationCount = row.hcursor.get[Int]("violation_count").getOrElse(0)
            val volunteerHours = row.hcursor.get[Int]("volunteer_hours").getOrElse(0)
            val completedTaskIds = row.hcursor.get[List[Int]]("completed_task_ids").getOrElse(List())

            StudentInfoResponse(
              userName,
              number,
              volunteerStatus,
              floor,
              sectionNumber,
              seatNumber,
              violationCount,
              volunteerHours,
              completedTaskIds
            ).asJson.noSpaces
          case None =>
            s"""{"error": "Student not found"}"""
        }
    }
  }
}
