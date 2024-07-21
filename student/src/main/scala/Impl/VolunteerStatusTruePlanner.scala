package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{VolunteerStatusTrueMessage, VolunteerStatusTrueResponse}

case class VolunteerStatusTruePlanner(message: VolunteerStatusTrueMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)", List(SqlParameter("String", message.number)))
      .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Student not found")))
      .flatMap(_ => writeDB(s"UPDATE $schemaName.students SET volunteer_status = true WHERE number = ?", List(SqlParameter("String", message.number)))
        .as(VolunteerStatusTrueResponse(success = true, message = s"Volunteer status updated to true for student with number: ${message.number}").asJson.noSpaces))
      .handleError(error => VolunteerStatusTrueResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
