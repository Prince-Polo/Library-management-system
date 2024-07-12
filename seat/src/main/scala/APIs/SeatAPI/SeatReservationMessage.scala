package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatReservationMessage(floor: String, section: String, seatNumber: String, studentNumber: String)

object SeatReservationMessage {
  implicit val encoder: Encoder[SeatReservationMessage] = deriveEncoder[SeatReservationMessage]
  implicit val decoder: Decoder[SeatReservationMessage] = deriveDecoder[SeatReservationMessage]
}
