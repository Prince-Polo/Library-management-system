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
    messageType match {
      case "StudentLoginMessage" =>
        IO(decode[StudentLoginMessage](str).getOrElse(throw new Exception("Invalid JSON for StudentLoginMessage")))
          .flatMap { m =>
            val traceID = TraceID(java.util.UUID.randomUUID().toString)
            val transactionLevel = 2
            val planContext = PlanContext(traceID, transactionLevel)
            val planner = StudentLoginMessagePlanner(m, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentQueryMessage" =>
        IO(decode[StudentReservationPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentQueryMessage")))
          .flatMap { m =>
            m.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentRegisterMessage" =>
        IO(decode[StudentRegisterMessage](str).getOrElse(throw new Exception("Invalid JSON for StudentRegisterMessage")))
          .flatMap { m =>
            val traceID = TraceID(java.util.UUID.randomUUID().toString)
            val transactionLevel = 1
            val planContext = PlanContext(traceID, transactionLevel)
            val planner = StudentRegisterPlanner(m, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentInfoMessage" =>
        IO(decode[StudentInfoMessage](str).getOrElse(throw new Exception("Invalid JSON for StudentInfoMessage")))
          .flatMap { m =>
            val traceID = TraceID(java.util.UUID.randomUUID().toString)
            val transactionLevel = 1
            val planContext = PlanContext(traceID, transactionLevel)
            val planner = StudentInfoPlanner(m, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentUnregisterMessage" =>
        IO(decode[StudentUnregisterMessage](str).getOrElse(throw new Exception("Invalid JSON for StudentUnregisterMessage")))
          .flatMap { m =>
            val traceID = TraceID(java.util.UUID.randomUUID().toString)
            val transactionLevel = 1
            val planContext = PlanContext(traceID, transactionLevel)
            val planner = StudentUnregisterPlanner(m, planContext)
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentUpdateMessage" =>
        IO(decode[StudentUpdateMessage](str).getOrElse(throw new Exception("Invalid JSON for StudentUpdateMessage")))
          .flatMap { m =>
            val traceID = TraceID(java.util.UUID.randomUUID().toString)
            val transactionLevel = 1
            val planContext = PlanContext(traceID, transactionLevel)
            val planner = StudentUpdatePlanner(m, planContext)
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
