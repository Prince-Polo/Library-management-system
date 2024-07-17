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

case class StudentSeatLeavePlanner(
                                    token: String,
                                    floor: String,
                                    section: String,
                                    seatNumber: String,
                                    override val planContext: PlanContext
                                  ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    startTransaction {
      for {
        // Validate token and retrieve student number
        studentNumberOpt <- IO(JWTUtil.getNumber(token))
        studentNumber <- studentNumberOpt match {
          case Some(number) => IO.pure(number)
          case None => IO.raiseError(new Exception("Invalid token"))
        }

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
