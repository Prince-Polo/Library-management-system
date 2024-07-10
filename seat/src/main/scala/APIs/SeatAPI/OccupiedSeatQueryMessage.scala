package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class OccupiedSeatQueryMessage()

object OccupiedSeatQueryMessage {
  implicit val encoder: Encoder[OccupiedSeatQueryMessage] = deriveEncoder[OccupiedSeatQueryMessage]
  implicit val decoder: Decoder[OccupiedSeatQueryMessage] = deriveDecoder[OccupiedSeatQueryMessage]
}
