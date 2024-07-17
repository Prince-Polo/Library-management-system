package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentDeleteMessage(number: String, password: String)

object StudentDeleteMessage {
  implicit val encoder: Encoder[StudentDeleteMessage] = deriveEncoder[StudentDeleteMessage]
  implicit val decoder: Decoder[StudentDeleteMessage] = deriveDecoder[StudentDeleteMessage]
}
