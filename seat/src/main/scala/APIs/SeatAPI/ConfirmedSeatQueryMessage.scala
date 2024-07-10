package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ConfirmedSeatQueryMessage()

object ConfirmedSeatQueryMessage {
  implicit val encoder: Encoder[ConfirmedSeatQueryMessage] = deriveEncoder[ConfirmedSeatQueryMessage]
  implicit val decoder: Decoder[ConfirmedSeatQueryMessage] = deriveDecoder[ConfirmedSeatQueryMessage]
}
