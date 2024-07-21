package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.decode
import Common.API.{PlanContext, Planner}
import APIs.StudentAPI.SubmitJobResponse
import APIs.JobAPI.SubmitTaskMessage
import Utils.JWTUtil

case class SubmitJobPlanner(token: String, jobId: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(studentNumber =>
        SubmitTaskMessage(jobId, studentNumber).send
          .flatMap(result => IO.fromEither(decode[SubmitJobResponse](result)))
          .flatMap(taskResponse => IO.raiseWhen(!taskResponse.success)(new Exception(taskResponse.message)))
          .as(SubmitJobResponse(success = true, message = "Job submitted successfully").asJson.noSpaces)
      ).handleError(error => SubmitJobResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
