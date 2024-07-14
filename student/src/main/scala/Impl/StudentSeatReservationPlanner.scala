package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{startTransaction, writeDB}
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

import APIs.StudentAPI.{StudentReservationMessage, StudentReservationResponse, StudentSeatReservationResponse}
import APIs.SeatAPI.{SeatReservationMessage, SeatReservationResponse}
import io.circe.parser.decode

case class StudentSeatReservationPlanner(
                                          studentNumber: String,
                                          floor: String,
                                          section: String,
                                          seatNumber: String,
                                          override val planContext: PlanContext
                                        ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    startTransaction {
      for {
        // Call student reservation planner
        studentReservationResult <- StudentReservationMessage(
          studentNumber,
          floor,
          section,
          seatNumber
        ).send

        studentResponse <- IO.fromEither(decode[StudentReservationResponse](studentReservationResult))
        _ <- if (studentResponse.success) IO.unit else IO.raiseError(new Exception(studentResponse.message))

        // Call seat reservation planner
        seatReservationResult <- SeatReservationMessage(
          floor,
          section,
          seatNumber,
          studentNumber
        ).send

        seatResponse <- IO.fromEither(decode[SeatReservationResponse](seatReservationResult))
        _ <- if (seatResponse.success) IO.unit else IO.raiseError(new Exception(seatResponse.message))

      } yield {
        StudentSeatReservationResponse(success = true, message = "Reservation successful").asJson.noSpaces
      }
    }.handleErrorWith { error =>
      IO.pure(StudentSeatReservationResponse(success = false, message = error.getMessage).asJson.noSpaces)
    }
  }
}
