package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import APIs.StudentAPI.{StudentSpecificAcceptedJobMessage, StudentSpecificAcceptedJobResponse}
import APIs.JobAPI.{FetchTasksMessage, FetchTasksResponse}
import Utils.JWTUtil

case class StudentSpecificAcceptedJobPlanner(token: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(studentNumber =>
        FetchTasksMessage(studentNumber).send
          .flatMap(result => IO.fromEither(decode[FetchTasksResponse](result)))
          .map(fetchTasksResponse =>
            StudentSpecificAcceptedJobResponse(success = true, message = "Tasks fetched successfully", tasks = Some(fetchTasksResponse.tasks)).asJson.noSpaces
          )
      ).handleError(error =>
        StudentSpecificAcceptedJobResponse(success = false, message = error.getMessage, tasks = None).asJson.noSpaces
      )
}
