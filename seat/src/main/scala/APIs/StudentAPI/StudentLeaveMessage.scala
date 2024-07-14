package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLeaveMessage(studentNumber: String, floor: String, section: String, seatNumber: String) extends StudentMessage[String]

object StudentLeaveMessage {
  implicit val encoder: Encoder[StudentLeaveMessage] = deriveEncoder[StudentLeaveMessage]
  implicit val decoder: Decoder[StudentLeaveMessage] = deriveDecoder[StudentLeaveMessage]
}
