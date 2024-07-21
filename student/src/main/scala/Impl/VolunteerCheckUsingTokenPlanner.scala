package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import APIs.StudentAPI.{VolunteerCheckUsingTokenResponse}
import APIs.JobAPI.CheckStudentTaskStatusMessage
import Utils.JWTUtil

case class VolunteerCheckUsingTokenPlanner(token: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(studentNumber =>
        CheckStudentTaskStatusMessage(studentNumber).send
          .flatMap(result => IO.fromEither(decode[Boolean](result)))
          .map(isVolunteer => VolunteerCheckUsingTokenResponse(isVolunteer, if (isVolunteer) "Student is a volunteer" else "Student is not a volunteer").asJson.noSpaces)
      ).handleError(error => VolunteerCheckUsingTokenResponse(isVolunteer = false, message = error.getMessage).asJson.noSpaces)
}
