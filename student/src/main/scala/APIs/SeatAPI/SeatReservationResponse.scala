package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatReservationResponse(success: Boolean, message: String)

object SeatReservationResponse {
  implicit val encoder: Encoder[SeatReservationResponse] = deriveEncoder[SeatReservationResponse]
  implicit val decoder: Decoder[SeatReservationResponse] = deriveDecoder[SeatReservationResponse]
}
