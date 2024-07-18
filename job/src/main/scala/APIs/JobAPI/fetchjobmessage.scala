package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

case class FetchTasksMessage(studentId: String) extends JobMessage[String]

object FetchTasksMessage {
  implicit val encoder: Encoder[FetchTasksMessage] = deriveEncoder[FetchTasksMessage]
  implicit val decoder: Decoder[FetchTasksMessage] = deriveDecoder[FetchTasksMessage]
}

case class TaskInfo(jobId: Int, status: Int)

object TaskInfo {
  implicit val encoder: Encoder[TaskInfo] = deriveEncoder[TaskInfo]
  implicit val decoder: Decoder[TaskInfo] = deriveDecoder[TaskInfo]
}

case class FetchTasksResponse(tasks: List[TaskInfo])

object FetchTasksResponse {
  implicit val encoder: Encoder[FetchTasksResponse] = deriveEncoder[FetchTasksResponse]
  implicit val decoder: Decoder[FetchTasksResponse] = deriveDecoder[FetchTasksResponse]
}
