package APIs.SeatAPI

import APIs.StudentAPI.StudentMessage
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatReservationMessage(floor: String, section: String, seatNumber: String, studentNumber: String) extends SeatMessage[String]

object SeatReservationMessage {
  implicit val encoder: Encoder[SeatReservationMessage] = deriveEncoder[SeatReservationMessage]
  implicit val decoder: Decoder[SeatReservationMessage] = deriveDecoder[SeatReservationMessage]
}
