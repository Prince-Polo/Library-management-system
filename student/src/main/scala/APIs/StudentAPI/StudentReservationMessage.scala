package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentReservationMessage(studentNumber: String, floor: String, section: String, seatNumber: String)

object StudentReservationMessage {
  implicit val encoder: Encoder[StudentReservationMessage] = deriveEncoder[StudentReservationMessage]
  implicit val decoder: Decoder[StudentReservationMessage] = deriveDecoder[StudentReservationMessage]
}
