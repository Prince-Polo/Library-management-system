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
import APIs.SeatAPI._  // 确保导入 SeatAPI

import java.util.UUID

object Routes:
  private def executePlan(messageType: String, str: String)(using planContext: PlanContext): IO[String] =
    messageType match {
      case "SeatQueryMessage" =>
        IO(decode[SeatQueryMessage](str).getOrElse(throw new Exception("Invalid JSON for SeatQueryMessage")))
          .flatMap { message =>
            val planner = SeatQueryPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "AddSeatMessage" =>
        IO(decode[AddSeatMessage](str).getOrElse(throw new Exception("Invalid JSON for AddSeatMessage")))
          .flatMap { message =>
            val planner = AddSeatPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "DeleteSeatMessage" =>
        IO(decode[DeleteSeatMessage](str).getOrElse(throw new Exception("Invalid JSON for DeleteSeatMessage")))
          .flatMap { message =>
            val planner = DeleteSeatPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "ReportedSeatQueryMessage" =>
        IO(decode[ReportedSeatQueryMessage](str).getOrElse(throw new Exception("Invalid JSON for ReportedSeatQueryMessage")))
          .flatMap { message =>
            val planner = ReportedSeatQueryPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "ConfirmedSeatQueryMessage" =>
        IO(decode[ConfirmedSeatQueryMessage](str).getOrElse(throw new Exception("Invalid JSON for ConfirmedSeatQueryMessage")))
          .flatMap { message =>
            val planner = ConfirmedSeatQueryPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "OccupiedSeatQueryMessage" =>
        IO(decode[OccupiedSeatQueryMessage](str).getOrElse(throw new Exception("Invalid JSON for OccupiedSeatQueryMessage")))
          .flatMap { message =>
            val planner = OccupiedSeatQueryPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "AvailableSeatQueryMessage" =>
        IO(decode[AvailableSeatQueryMessage](str).getOrElse(throw new Exception("Invalid JSON for AvailableSeatQueryMessage")))
          .flatMap { message =>
            val planner = AvailableSeatQueryPlanner(message, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case _ =>
        IO.raiseError(new Exception(s"Unknown type: $messageType"))
    }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO]:
    case req @ POST -> Root / "api" / name =>
      given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), transactionLevel = 0)
      println("request received")
      req.as[String].flatMap { executePlan(name, _) }.flatMap(Ok(_))
        .handleErrorWith { e =>
          println(e)
          BadRequest(e.getMessage)
        }
