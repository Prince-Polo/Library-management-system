package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{SubmitJobMessage, SubmitJobResponse}
import APIs.JobAPI.SubmitTaskMessage
import Utils.JWTUtil

case class SubmitJobPlanner(
                             token: String,
                             jobId: Int,
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

      // Call job service to submit a task
      submitTaskMessage = SubmitTaskMessage(jobId, studentNumber)
      taskSubmissionResult <- submitTaskMessage.send

      taskResponse <- IO.fromEither(decode[SubmitJobResponse](taskSubmissionResult))
      _ <- if (taskResponse.success) IO.unit else IO.raiseError(new Exception(taskResponse.message))

    } yield {
      SubmitJobResponse(success = true, message = "Job submitted successfully").asJson.noSpaces
    }
  }.handleErrorWith { error =>
    IO.pure(SubmitJobResponse(success = false, message = error.getMessage).asJson.noSpaces)
  }
}
