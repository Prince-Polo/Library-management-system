package Process

import Common.API.PlanContext
import Impl.*
import cats.effect.*
import io.circe.generic.auto.*
import io.circe.parser.decode
import io.circe.syntax.*
import org.http4s.*
import org.http4s.client.Client
import org.http4s.dsl.io.*


object Routes:
  private def executePlan(messageType:String, str: String): IO[String]=
    messageType match {
    case "AddJobMessage" =>
        IO(decode[AddJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for AddJobMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
/*    case "AvailableJobQueryMessage" =>
      IO(decode[AvailableJobQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for AvailableJobQuery")))
        .flatMap{m=>
          m.fullPlan.map(_.asJson.toString)
        }
    case "BookedCompletedUnapprovedJobQueryMessage" =>
      IO(decode[BookedCompletedUnapprovedJobQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for BookedCompletedUnapprovedJobQueryMessage")))
        .flatMap{m=>
          m.fullPlan.map(_.asJson.toString)
        }*/
    case "ApprovedJobMessage" =>
      IO(decode[ApprovedJobQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for ApprovedJobMessage")))
        .flatMap{m=>
          m.fullPlan.map(_.asJson.toString)
        }
    case "DeleteJobMessage" =>
      IO(decode[DeleteJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for DeleteJobMessage")))
        .flatMap { m=>
          m.fullPlan.map(_.asJson.toString)
        }
    case _ =>
      IO.raiseError(new Exception(s"Unknown type: $messageType"))
    }
/*    case "BookedIncompleteJobMessage" =>
      IO(decode[BookedIncompleteJobQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for BookedIncompleteJobMessage")))
        .flatMap{m=>
          m.fullPlan.map(_.asJson.toString)
        } */


  val service: HttpRoutes[IO] = HttpRoutes.of[IO]:
    case req @ POST -> Root / "api" / name =>
        println("request received")
        req.as[String].flatMap{executePlan(name, _)}.flatMap(Ok(_))
        .handleErrorWith{e =>
          println(e)
          BadRequest(e.getMessage)
        }
