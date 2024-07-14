package Process

import Common.API.{PlanContext, TraceID}
import Impl._
import cats.effect._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.client.Client
import APIs.SeatAPI._
import Utils.JWTUtil

import java.util.UUID

object Routes:
  private def executePlan(messageType: String, str: String, token: String): IO[String] =
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), transactionLevel = 0)

    if (!JWTUtil.validateToken(token)) {
      IO.raiseError(new Exception("Invalid token"))
    } else {
      messageType match {
        case "SeatQueryMessage" =>
          IO(decode[SeatQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatQueryMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "AddSeatMessage" =>
          IO(decode[AddSeatPlanner](str).getOrElse(throw new Exception("Invalid JSON for AddSeatMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "DeleteSeatMessage" =>
          IO(decode[DeleteSeatPlanner](str).getOrElse(throw new Exception("Invalid JSON for DeleteSeatMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "ReportedSeatQueryMessage" =>
          IO(decode[ReportedSeatQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for ReportedSeatQueryMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "ConfirmedSeatQueryMessage" =>
          IO(decode[ConfirmedSeatQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for ConfirmedSeatQueryMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "OccupiedSeatQueryMessage" =>
          IO(decode[OccupiedSeatQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for OccupiedSeatQueryMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "AvailableSeatQueryMessage" =>
          IO(decode[AvailableSeatQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for AvailableSeatQueryMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "SeatReservationMessage" =>
          IO(decode[SeatReservationPlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatReservationMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "SeatLeaveMessage" =>
          IO(decode[SeatLeavePlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatLeaveMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "SeatReportMessage" =>
          IO(decode[SeatReportPlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatReportMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "SeatConfirmMessage" =>
          IO(decode[SeatConfirmPlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatConfirmMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "SeatRefreshMessage" =>
          IO(decode[SeatRefreshPlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatRefreshMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case _ =>
          IO.raiseError(new Exception(s"Unknown type: $messageType"))
      }
    }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO]:
    case req @ POST -> Root / "api" / name =>
      req.headers.get(CIString("Authorization")) match {
        case Some(tokenHeader) =>
          val token = tokenHeader.value
          req.as[String].flatMap { executePlan(name, _, token) }.flatMap(Ok(_))
            .handleErrorWith { e =>
              BadRequest(e.getMessage)
            }
        case None =>
          BadRequest("Missing Authorization header")
      }
