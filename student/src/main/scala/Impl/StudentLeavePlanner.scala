package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{StudentLeaveResponse}

case class StudentLeavePlanner(
                                studentNumber: String,
                                floor: String,
                                section: String,
                                seatNumber: String,
                                override val planContext: PlanContext
                              ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.students SET floor = '0', section_number = '0', seat_number = '0' WHERE number = ?",
      List(
        SqlParameter("String", studentNumber)
      )
    ).map { rowsAffected =>
      if (rowsAffected!="") {
        StudentLeaveResponse(success = true, message = s"Student seat cleared successfully for student: $studentNumber").asJson.noSpaces
      } else {
        StudentLeaveResponse(success = false, message = s"Failed to clear seat for student: $studentNumber").asJson.noSpaces
      }
    }
  }
}
