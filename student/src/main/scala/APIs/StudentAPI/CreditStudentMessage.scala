package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CreditStudentMessage(number: String, volunteerHours: String)

object CreditStudentMessage {
  implicit val encoder: Encoder[CreditStudentMessage] = deriveEncoder[CreditStudentMessage]
  implicit val decoder: Decoder[CreditStudentMessage] = deriveDecoder[CreditStudentMessage]
}
