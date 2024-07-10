package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AvailableSeatQueryMessage()

object AvailableSeatQueryMessage {
  implicit val encoder: Encoder[AvailableSeatQueryMessage] = deriveEncoder[AvailableSeatQueryMessage]
  implicit val decoder: Decoder[AvailableSeatQueryMessage] = deriveDecoder[AvailableSeatQueryMessage]
}
