package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import APIs.StudentAPI.AcceptJobResponse
import APIs.JobAPI.CreateTaskMessage
import Utils.JWTUtil

case class AcceptJobPlanner(token: String, jobId: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(CreateTaskMessage(jobId, _).send)
      .flatMap(result => IO.fromEither(decode[AcceptJobResponse](result)))
      .flatMap(taskResponse => IO.raiseWhen(!taskResponse.success)(new Exception(taskResponse.message)))
      .as(AcceptJobResponse(success = true, message = "Job accepted successfully").asJson.noSpaces)
      .handleError(error => AcceptJobResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
