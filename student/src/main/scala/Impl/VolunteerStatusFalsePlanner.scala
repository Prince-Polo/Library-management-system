package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.VolunteerStatusFalseResponse

case class VolunteerStatusFalsePlanner(number: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)", List(SqlParameter("String", number)))
      .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Student not found")))
      .flatMap(_ => writeDB(s"UPDATE $schemaName.students SET volunteer_status = false WHERE number = ?", List(SqlParameter("String", number)))
        .as(VolunteerStatusFalseResponse(success = true, message = s"Volunteer status updated to false for student with number: $number").asJson.noSpaces))
      .handleError(error => VolunteerStatusFalseResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
