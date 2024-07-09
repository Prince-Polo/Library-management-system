package Process

import Common.API.PlanContext
import Impl._
import cats.effect._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import APIs.PatientAPI._
import APIs.SeatAPI._  // 确保导入 SeatAPI

object Routes:
  private def executePlan(messageType: String, str: String): IO[String] =
    messageType match {
      case "SeatQueryMessage" =>
        IO(decode[SeatQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for SeatQueryMessage")))
          .flatMap { m =>
            m.fullPlan.map(_.asJson.noSpaces)
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
