package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{VolunteerCheckUsingTokenMessage, VolunteerCheckUsingTokenResponse}
import APIs.JobAPI.VolunteerCheckMessage
import Utils.JWTUtil

case class VolunteerCheckUsingTokenPlanner(
                                            token: String,
                                            override val planContext: PlanContext
                                          ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // Validate token and retrieve student number
      studentNumberOpt <- IO(JWTUtil.getNumber(token))
      studentNumber <- studentNumberOpt match {
        case Some(number) => IO.pure(number)
        case None => IO.raiseError(new Exception("Invalid token"))
      }

      // Call job service to check volunteer status
      volunteerCheckMessage = VolunteerCheckMessage(studentNumber)
      volunteerCheckResult <- volunteerCheckMessage.send // Send the message and wait for a response

      // Decode the response
      isVolunteer <- IO.fromEither(decode[Boolean](volunteerCheckResult))

      // Prepare response
      response = VolunteerCheckUsingTokenResponse(
        isVolunteer = isVolunteer,
        message = if (isVolunteer) "Student is a volunteer" else "Student is not a volunteer"
      )

    } yield {
      response.asJson.noSpaces
    }
  }.handleErrorWith { error =>
    IO.pure(VolunteerCheckUsingTokenResponse(isVolunteer = false, message = error.getMessage).asJson.noSpaces)
  }
}
