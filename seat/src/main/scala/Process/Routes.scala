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
import Common.SeatPosition

import java.util.UUID

object Routes:
  private def executePlan(messageType: String, str: String): IO[String] =
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), transactionLevel = 0)

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
      case "QueryAllFloorsMessage" =>
        IO(decode[QueryAllFloorsPlanner](str).getOrElse(throw new Exception("Invalid JSON for QueryAllFloorsMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "QuerySectionsByFloorMessage" =>
        IO(decode[QuerySectionsByFloorPlanner](str).getOrElse(throw new Exception("Invalid JSON for QuerySectionsByFloorMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "QuerySeatsInSectionMessage" =>
        IO(decode[QuerySeatsInSectionPlanner](str).getOrElse(throw new Exception("Invalid JSON for QuerySeatsInSectionMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "UpdateSeatOccupiedStatus" =>
        IO(decode[OccupySeatPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateSeatOccupiedStatus")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "UpdateSeatReleasedStatus" =>
        IO(decode[ReleaseSeatPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateSeatReleasedStatus")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case _ =>
        IO.raiseError(new Exception(s"Unknown type: $messageType"))
    }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO]:
    case req @ POST -> Root / "api" / name =>
      println("request received")
      req.as[String].flatMap { executePlan(name, _) }.flatMap(Ok(_))
        .handleErrorWith { e =>
          println(e)
          BadRequest(e.getMessage)
        }
