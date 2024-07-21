package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBBoolean, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.VolunteerStatusFalseUsingTokenResponse
import Utils.JWTUtil

case class VolunteerStatusFalseUsingTokenPlanner(token: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(number =>
        readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE number = ?)", List(SqlParameter("String", number)))
          .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Student not found")))
          .flatMap(_ => writeDB(s"UPDATE $schemaName.students SET volunteer_status = false WHERE number = ?", List(SqlParameter("String", number)))
            .as(VolunteerStatusFalseUsingTokenResponse(success = true, message = s"Volunteer status updated to false for student with number: $number").asJson.noSpaces))
      ).handleError(error => VolunteerStatusFalseUsingTokenResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
