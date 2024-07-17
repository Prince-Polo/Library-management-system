package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import APIs.StudentAPI.{StudentSpecificAcceptedJobMessage, StudentSpecificAcceptedJobResponse}
import APIs.JobAPI.{FetchTasksMessage, FetchTasksResponse}
import Utils.JWTUtil

case class StudentSpecificAcceptedJobPlanner(
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

      // Call job service to get specific accepted tasks
      specificAcceptedTaskMessage = FetchTasksMessage(studentNumber)
      taskFetchResult <- specificAcceptedTaskMessage.send // Send the message and wait for a response

      // Decode the response
      fetchTasksResponse <- IO.fromEither(decode[FetchTasksResponse](taskFetchResult))

    } yield {
      StudentSpecificAcceptedJobResponse(success = true, message = "Tasks fetched successfully", tasks = Some(fetchTasksResponse.tasks)).asJson.noSpaces
    }
  }.handleErrorWith { error =>
    IO.pure(StudentSpecificAcceptedJobResponse(success = false, message = error.getMessage, tasks = None).asJson.noSpaces)
  }
}
