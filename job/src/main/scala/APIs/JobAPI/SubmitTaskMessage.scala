package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.API.API

case class SubmitTaskMessage(jobId: Int, studentId: String) extends JobMessage[String]

object SubmitTaskMessage {
  implicit val encoder: Encoder[SubmitTaskMessage] = deriveEncoder[SubmitTaskMessage]
  implicit val decoder: Decoder[SubmitTaskMessage] = deriveDecoder[SubmitTaskMessage]
}

case class SubmitTaskResponse(success: Boolean, message: String)

object SubmitTaskResponse {
  implicit val encoder: Encoder[SubmitTaskResponse] = deriveEncoder[SubmitTaskResponse]
  implicit val decoder: Decoder[SubmitTaskResponse] = deriveDecoder[SubmitTaskResponse]
}
