package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Common.BasicInfo
import APIs.StudentAPI.{StudentRegisterResponse}

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
          s"INSERT INTO ${schemaName}.students (user_name, password, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours, completed_task_ids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
          List(
            SqlParameter("String", info.userName),
            SqlParameter("String", info.password),
            SqlParameter("String", info.number),
            SqlParameter("Boolean", "false"),  // initial volunteer status
            SqlParameter("Int", "0"),  // initial floor
            SqlParameter("Int", "0"),  // initial section_number
            SqlParameter("Int", "0"),  // initial seat_number
            SqlParameter("Int", "0"),  // initial violation_count
            SqlParameter("Int", "0"),  // initial volunteer_hours
            SqlParameter("Array", "{}")  // initial completed_task_ids (empty array)
          )
        ).map { _ =>
          StudentRegisterResponse(
            userName = info.userName,
            number = info.number,
            volunteerStatus = false,
            floor = 0,
            sectionNumber = 0,
            seatNumber = 0,
            violationCount = 0,
            volunteerHours = 0,
            completedTaskIds = List.empty
          ).asJson.noSpaces
        }
      }
    }
  }
}
