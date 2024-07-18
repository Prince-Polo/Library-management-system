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
import Common.BasicInfo

import java.util.UUID

object Routes:
  private def executePlan(messageType: String, str: String): IO[String] =
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), transactionLevel = 0)

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
      case "StudentSeatReservationMessage" =>
        IO(decode[StudentSeatReservationPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentSeatReservationMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentSeatLeaveMessage" =>
        IO(decode[StudentSeatLeavePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentSeatLeaveMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentInfoUsingTokenMessage" =>
        IO(decode[StudentInfoUsingTokenPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentInfoUsingTokenMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "AcceptJobMessage" =>
        IO(decode[AcceptJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for AcceptJobMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentDeleteMessage" =>
        IO(decode[StudentDeletePlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentDeleteMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "VolunteerStatusTrueMessage" =>
        IO(decode[VolunteerStatusTruePlanner](str).getOrElse(throw new Exception("Invalid JSON for VolunteerStatusTrueMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "VolunteerStatusFalseMessage" =>
        IO(decode[VolunteerStatusFalsePlanner](str).getOrElse(throw new Exception("Invalid JSON for VolunteerStatusFalseMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "StudentSpecificAcceptedJobMessage" =>
        IO(decode[StudentSpecificAcceptedJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for StudentSpecificAcceptedJobMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "VolunteerStatusFalseUsingTokenMessage" =>
        IO(decode[VolunteerStatusFalseUsingTokenPlanner](str).getOrElse(throw new Exception("Invalid JSON for VolunteerStatusFalseUsingTokenMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "SubmitJobMessage" =>
        IO(decode[SubmitJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for SubmitJobMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "UpdateVolunteerHourMessage" =>
        IO(decode[UpdateVolunteerHourPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateTaskStatusMessage")))
          .flatMap { planner =>
            planner.fullPlan.map(_.asJson.noSpaces)
          }
      case "VolunteerCheckUsingTokenMessage" =>
        IO(decode[VolunteerCheckUsingTokenPlanner](str).getOrElse(throw new Exception("Invalid JSON for VolunteerCheckUsingTokenMessage")))
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
