package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentInfoUsingTokenMessage(token: String)

object StudentInfoUsingTokenMessage {
  implicit val encoder: Encoder[StudentInfoUsingTokenMessage] = deriveEncoder[StudentInfoUsingTokenMessage]
  implicit val decoder: Decoder[StudentInfoUsingTokenMessage] = deriveDecoder[StudentInfoUsingTokenMessage]
}
