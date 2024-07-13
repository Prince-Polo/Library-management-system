package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatConfirmResponse(success: Boolean, message: String)

object SeatConfirmResponse {
  implicit val encoder: Encoder[SeatConfirmResponse] = deriveEncoder[SeatConfirmResponse]
  implicit val decoder: Decoder[SeatConfirmResponse] = deriveDecoder[SeatConfirmResponse]
}
