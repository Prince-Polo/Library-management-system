package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{startTransaction, writeDB, readDBRows}
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

import APIs.StudentAPI.{StudentSeatReservationMessage, StudentSeatReservationResponse, StudentReservationMessage, StudentReservationResponse}
import APIs.SeatAPI.{SeatReservationMessage, SeatReservationResponse}
import io.circe.parser.decode
import Utils.JWTUtil

case class StudentSeatReservationPlanner(
                                          token: String,
                                          floor: String,
                                          section: String,
                                          seatNumber: String,
                                          override val planContext: PlanContext
                                        ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    startTransaction {
      for {
        // Validate token and retrieve student ID
        userNameOpt <- IO(JWTUtil.getUserName(token))
        userName <- userNameOpt match {
          case Some(name) => IO.pure(name)
          case None => IO.raiseError(new Exception("Invalid token"))
        }

        // Retrieve student number from the database
        studentNumberRows <- readDBRows(
          s"SELECT number FROM ${schemaName}.students WHERE user_name = ?",
          List(SqlParameter("String", userName))
        )
        studentNumber <- studentNumberRows.headOption match {
          case Some(row) => IO.pure(row.hcursor.get[String]("number").getOrElse(""))
          case None => IO.raiseError(new Exception("Student not found"))
        }

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
