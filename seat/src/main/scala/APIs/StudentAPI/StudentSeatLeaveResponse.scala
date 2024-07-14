package APIs.StudentAPI

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

case class StudentSeatLeaveResponse(success: Boolean, message: String)

object StudentSeatLeaveResponse {
  implicit val encoder: Encoder[StudentSeatLeaveResponse] = deriveEncoder[StudentSeatLeaveResponse]
  implicit val decoder: Decoder[StudentSeatLeaveResponse] = deriveDecoder[StudentSeatLeaveResponse]
}
