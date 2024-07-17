package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{AcceptJobMessage, AcceptJobResponse}
import APIs.JobAPI.CreateTaskMessage
import Utils.JWTUtil

case class AcceptJobPlanner(
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

      // Call job service to create a task
      createTaskMessage = CreateTaskMessage(jobId, studentNumber)
      taskCreationResult <- createTaskMessage.send

      taskResponse <- IO.fromEither(decode[AcceptJobResponse](taskCreationResult))
      _ <- if (taskResponse.success) IO.unit else IO.raiseError(new Exception(taskResponse.message))

    } yield {
      AcceptJobResponse(success = true, message = "Job accepted successfully").asJson.noSpaces
    }
  }.handleErrorWith { error =>
    IO.pure(AcceptJobResponse(success = false, message = error.getMessage).asJson.noSpaces)
  }
}
