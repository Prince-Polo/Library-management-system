package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{startTransaction, writeDB, readDBRows}
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{StudentSeatLeaveMessage, StudentSeatLeaveResponse, StudentLeaveMessage, StudentLeaveResponse}
import APIs.SeatAPI.{SeatLeaveMessage, SeatLeaveResponse}
import io.circe.parser.decode
import Utils.JWTUtil

case class StudentSeatLeavePlanner(token: String, floor: String, section: String, seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    startTransaction(
      IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
        .flatMap(studentNumber =>
          StudentLeaveMessage(studentNumber, floor, section, seatNumber).send
            .flatMap(result => IO.fromEither(decode[StudentLeaveResponse](result)))
            .flatMap(studentResponse => IO.raiseWhen(!studentResponse.success)(new Exception(studentResponse.message)))
            .flatMap(_ => SeatLeaveMessage(floor, section, seatNumber, studentNumber).send)
            .flatMap(result => IO.fromEither(decode[SeatLeaveResponse](result)))
            .flatMap(seatResponse => IO.raiseWhen(!seatResponse.success)(new Exception(seatResponse.message)))
            .as(StudentSeatLeaveResponse(success = true, message = "Leave successful").asJson.noSpaces)
        )
    ).handleError(error => StudentSeatLeaveResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
