package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import APIs.JobAPI.TaskInfo
import Common.API.*

case class StudentSpecificAcceptedJobResponse(success: Boolean, message: String, tasks: Option[List[TaskInfo]])

object StudentSpecificAcceptedJobResponse {
  implicit val encoder: Encoder[StudentSpecificAcceptedJobResponse] = deriveEncoder[StudentSpecificAcceptedJobResponse]
  implicit val decoder: Decoder[StudentSpecificAcceptedJobResponse] = deriveDecoder[StudentSpecificAcceptedJobResponse]
}
