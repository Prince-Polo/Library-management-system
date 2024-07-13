package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatConfirmMessage(
                               floor: String,
                               section: String,
                               seatNumber: String,
                               feedback: String
                             )

object SeatConfirmMessage {
  implicit val encoder: Encoder[SeatConfirmMessage] = deriveEncoder[SeatConfirmMessage]
  implicit val decoder: Decoder[SeatConfirmMessage] = deriveDecoder[SeatConfirmMessage]
}
