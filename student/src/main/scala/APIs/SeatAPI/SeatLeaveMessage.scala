package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatLeaveMessage(floor: String, section: String, seatNumber: String, studentNumber: String) extends SeatMessage[String]

object SeatLeaveMessage {
  implicit val encoder: Encoder[SeatLeaveMessage] = deriveEncoder[SeatLeaveMessage]
  implicit val decoder: Decoder[SeatLeaveMessage] = deriveDecoder[SeatLeaveMessage]
}
