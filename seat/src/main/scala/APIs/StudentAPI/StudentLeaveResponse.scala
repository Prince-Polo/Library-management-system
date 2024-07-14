package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLeaveResponse(success: Boolean, message: String)

object StudentLeaveResponse {
  implicit val encoder: Encoder[StudentLeaveResponse] = deriveEncoder[StudentLeaveResponse]
  implicit val decoder: Decoder[StudentLeaveResponse] = deriveDecoder[StudentLeaveResponse]
}
