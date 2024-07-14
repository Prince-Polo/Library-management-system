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
import APIs.StudentAPI._
import Utils.JWTUtil

import java.util.UUID

object Routes:
  private def executePlan(messageType: String, str: String, token: String): IO[String] =
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), transactionLevel = 0)

    if (!JWTUtil.validateToken(token)) {
      IO.raiseError(new Exception("Invalid token"))
    } else {
      messageType match {
        case "StudentRegisterMessage" =>
          IO(decode[StudentRegisterPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentRegisterMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "StudentLoginMessage" =>
          IO(decode[StudentLoginMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentLoginMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "StudentInfoMessage" =>
          IO(decode[StudentInfoPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentInfoMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "StudentUpdateMessage" =>
          IO(decode[StudentUpdatePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentUpdateMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "StudentUnregisterMessage" =>
          IO(decode[StudentUnregisterPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentUnregisterMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "StudentReservationMessage" =>
          IO(decode[StudentReservationPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentReservationMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "StudentLeaveMessage" =>
          IO(decode[StudentLeavePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentLeaveMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "CreditStudentMessage" =>
          IO(decode[CreditStudentPlanner](str).getOrElse(throw new Exception("Invalid JSON for CreditStudentMessage")))
            .flatMap { planner =>
              planner.fullPlan.map(_.asJson.noSpaces)
            }
        case "UpdateViolationCountMessage" =>
          IO(decode[UpdateViolationCountPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateViolationCountMessage")))
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
