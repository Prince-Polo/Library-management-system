package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{startTransaction, writeDB}
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

import APIs.StudentAPI.{StudentLeaveMessage, StudentLeaveResponse, StudentSeatLeaveResponse}
import APIs.SeatAPI.{SeatLeaveMessage, SeatLeaveResponse}
import io.circe.parser.decode

case class StudentSeatLeavePlanner(
                                    studentNumber: String,
                                    floor: String,
                                    section: String,
                                    seatNumber: String,
                                    override val planContext: PlanContext
                                  ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    startTransaction {
      for {
        // Call student leave planner
        studentLeaveResult <- StudentLeaveMessage(
          studentNumber,
          floor,
          section,
          seatNumber
        ).send

        studentResponse <- IO.fromEither(decode[StudentLeaveResponse](studentLeaveResult))
        _ <- if (studentResponse.success) IO.unit else IO.raiseError(new Exception(studentResponse.message))

        // Call seat leave planner
        seatLeaveResult <- SeatLeaveMessage(
          floor,
          section,
          seatNumber,
          studentNumber
        ).send

        seatResponse <- IO.fromEither(decode[SeatLeaveResponse](seatLeaveResult))
        _ <- if (seatResponse.success) IO.unit else IO.raiseError(new Exception(seatResponse.message))

      } yield {
        StudentSeatLeaveResponse(success = true, message = "Leave successful").asJson.noSpaces
      }
    }.handleErrorWith { error =>
      IO.pure(StudentSeatLeaveResponse(success = false, message = error.getMessage).asJson.noSpaces)
    }
  }
}
