package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentReservationResponse

case class StudentReservationPlanner(studentNumber: String, floor: String, section: String, seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    writeDB(s"UPDATE $schemaName.students SET floor = ?, section_number = ?, seat_number = ? WHERE number = ?", List(SqlParameter("String", floor), SqlParameter("String", section), SqlParameter("String", seatNumber), SqlParameter("String", studentNumber)))
      .map(rowsAffected => StudentReservationResponse(success = rowsAffected != "", message = if (rowsAffected != "") s"Student seat updated successfully for student: $studentNumber" else s"Failed to update seat for student: $studentNumber").asJson.noSpaces)
      .handleError(error => StudentReservationResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
