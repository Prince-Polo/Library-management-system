package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentInfoMessage(number: String)

object StudentInfoMessage {
  implicit val encoder: Encoder[StudentInfoMessage] = deriveEncoder[StudentInfoMessage]
  implicit val decoder: Decoder[StudentInfoMessage] = deriveDecoder[StudentInfoMessage]
}
