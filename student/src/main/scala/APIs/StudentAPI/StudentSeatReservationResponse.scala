package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentSeatReservationResponse(success: Boolean, message: String)

object StudentSeatReservationResponse {
  implicit val encoder: Encoder[StudentSeatReservationResponse] = deriveEncoder[StudentSeatReservationResponse]
  implicit val decoder: Decoder[StudentSeatReservationResponse] = deriveDecoder[StudentSeatReservationResponse]
}
