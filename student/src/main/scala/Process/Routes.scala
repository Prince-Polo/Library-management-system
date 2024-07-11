package Process

import Common.API.{PlanContext, TraceID}
import Impl._
import cats.effect._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import _root_.APIs.StudentAPI._

object Routes:
  private def executePlan(messageType: String, str: String): IO[String] = {
    given PlanContext = PlanContext(traceID = TraceID(java.util.UUID.randomUUID().toString), transactionLevel = 1)

    messageType match {
      case "StudentLoginMessage" =>
        IO(decode[StudentLoginMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentLoginMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentQueryMessage" =>
        IO(decode[StudentReservationPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentQueryMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentRegisterMessage" =>
        IO(decode[StudentRegisterPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentRegisterMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentInfoMessage" =>
        IO(decode[StudentInfoPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentInfoMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentUnregisterMessage" =>
        IO(decode[StudentUnregisterPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentUnregisterMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentUpdateMessage" =>
        IO(decode[StudentUpdatePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentUpdateMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case _ =>
        IO.raiseError(new Exception(s"Unknown type: $messageType"))
    }
  }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "api" / messageType =>
      req.as[String].flatMap { str =>
        executePlan(messageType, str).flatMap { result =>
          Ok(result)
        }.handleErrorWith {
          case e: Exception => BadRequest(e.getMessage)
        }
      }
  }
