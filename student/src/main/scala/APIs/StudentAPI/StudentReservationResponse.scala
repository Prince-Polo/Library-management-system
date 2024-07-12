package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentReservationResponse(success: Boolean, message: String)

object StudentReservationResponse {
  implicit val encoder: Encoder[StudentReservationResponse] = deriveEncoder[StudentReservationResponse]
  implicit val decoder: Decoder[StudentReservationResponse] = deriveDecoder[StudentReservationResponse]
}
