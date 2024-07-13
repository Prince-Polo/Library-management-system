package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AddSeatMessage(
                           floor: String,
                           section: String,
                           seatNumber: String
                         )

object AddSeatMessage {
  implicit val encoder: Encoder[AddSeatMessage] = deriveEncoder[AddSeatMessage]
  implicit val decoder: Decoder[AddSeatMessage] = deriveDecoder[AddSeatMessage]
}
