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
    case "ApprovedJobMessage" =>
      IO(decode[ApprovedJobQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for ApprovedJobMessage")))
        .flatMap{m=>
          m.fullPlan.map(_.asJson.toString)
        }
    case "ApprovedJobQueryMessage" =>
      IO(decode[ApprovedJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for ApprovedJobMessage")))
        .flatMap{m=>
          m.fullPlan.map(_.asJson.toString)
        }
    case "DeleteJobMessage" =>
      IO(decode[DeleteJobPlanner](str).getOrElse(throw new Exception("Invalid JSON for DeleteJobMessage")))
        .flatMap { m=>
          m.fullPlan.map(_.asJson.toString)
        }
    case "UpdateJobCurrentMessage"
    =>
      IO(decode[UpdateJobCurrentPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateJobCurrentMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
/*    case "UpdateTaskStatusMessage" =>
      IO(decode[UpdateTaskStatusPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateTaskStatusMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.noSpaces)
        }*/
    case "CreateTaskMessage"
    =>
      IO(decode[CreateTaskPlanner](str).getOrElse(throw new Exception("Invalid JSON for CreateTaskMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "CheckTask" =>
      IO(decode[CheckTaskPlanner](str).getOrElse(throw new Exception("Invalid JSON for CheckTask")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "JobStudentsQueryMessage" =>
      IO(decode[JobStudentsQueryPlanner](str).getOrElse(throw new Exception("Invalid JSON for JobStudentsQuery")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "FetchTasksMessage" =>
      IO(decode[FetchTasksPlanner](str).getOrElse(throw new Exception("Invalid JSON for FetchTasksMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "SubmitTaskMessage" =>
      IO(decode[SubmitTaskPlanner](str).getOrElse(throw new Exception("Invalid JSON for SUbmitTaskMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "ForceEndTaskMessage" =>
      IO(decode[ForceEndTaskPlanner](str).getOrElse(throw new Exception("Invalid JSON for ForceEndMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "UpdateTaskStatusMessage" =>
      IO(decode[UpdateTaskStatusPlanner](str).getOrElse(throw new Exception("Invalid JSON for UpdateTaskStatusMessage")))
        .flatMap { m =>
          m.fullPlan.map(_.asJson.toString)
        }
    case "CheckStudentTaskStatusMessage" =>
      IO(decode[CheckStudentTaskStatusPlanner](str).getOrElse(throw new Exception("Invalid JSON for CheckStudentStatusMessage")))
        .flatMap { m =>
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
