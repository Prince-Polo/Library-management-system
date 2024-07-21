package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.startTransaction
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.ServiceUtils.schemaName

import APIs.StudentAPI.{StudentSeatReservationResponse, StudentReservationMessage, StudentReservationResponse}
import APIs.SeatAPI.{SeatReservationMessage, SeatReservationResponse}
import io.circe.parser.decode
import Utils.JWTUtil

case class StudentSeatReservationPlanner(token: String, floor: String, section: String, seatNumber: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    startTransaction(
      IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
        .flatMap(studentNumber =>
          StudentReservationMessage(studentNumber, floor, section, seatNumber).send
            .flatMap(result => IO.fromEither(decode[StudentReservationResponse](result)))
            .flatMap(studentResponse => IO.raiseWhen(!studentResponse.success)(new Exception(studentResponse.message)))
            .flatMap(_ => SeatReservationMessage(floor, section, seatNumber, studentNumber).send)
            .flatMap(result => IO.fromEither(decode[SeatReservationResponse](result)))
            .flatMap(seatResponse => IO.raiseWhen(!seatResponse.success)(new Exception(seatResponse.message)))
            .as(StudentSeatReservationResponse(success = true, message = "Reservation successful").asJson.noSpaces)
        )
    ).handleError(error => StudentSeatReservationResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
