package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatRefreshResponse(success: Boolean, message: String)

object SeatRefreshResponse {
  implicit val encoder: Encoder[SeatRefreshResponse] = deriveEncoder[SeatRefreshResponse]
  implicit val decoder: Decoder[SeatRefreshResponse] = deriveDecoder[SeatRefreshResponse]
}
