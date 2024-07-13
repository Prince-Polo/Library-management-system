package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatRefreshMessage(
                               floor: String,
                               section: String,
                               seatNumber: String
                             )

object SeatRefreshMessage {
  implicit val encoder: Encoder[SeatRefreshMessage] = deriveEncoder[SeatRefreshMessage]
  implicit val decoder: Decoder[SeatRefreshMessage] = deriveDecoder[SeatRefreshMessage]
}
