package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatAvailableMessage()

object SeatAvailableMessage {
  implicit val encoder: Encoder[SeatAvailableMessage] = deriveEncoder[SeatAvailableMessage]
  implicit val decoder: Decoder[SeatAvailableMessage] = deriveDecoder[SeatAvailableMessage]
}
