package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentUpdateMessage(
                                 userName: String,
                                 password: String,
                                 number: String,
                                 newPassword: Option[String] = None
                               )

object StudentUpdateMessage {
  implicit val encoder: Encoder[StudentUpdateMessage] = deriveEncoder[StudentUpdateMessage]
  implicit val decoder: Decoder[StudentUpdateMessage] = deriveDecoder[StudentUpdateMessage]
}
