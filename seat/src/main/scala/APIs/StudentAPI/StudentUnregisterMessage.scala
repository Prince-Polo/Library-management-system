package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentUnregisterMessage(number: String)

object StudentUnregisterMessage {
  implicit val encoder: Encoder[StudentUnregisterMessage] = deriveEncoder[StudentUnregisterMessage]
  implicit val decoder: Decoder[StudentUnregisterMessage] = deriveDecoder[StudentUnregisterMessage]
}
