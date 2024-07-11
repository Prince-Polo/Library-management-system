package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentRegisterMessage(
                                   userName: String,
                                   password: String,
                                   number: String
                                 )

object StudentRegisterMessage {
  implicit val encoder: Encoder[StudentRegisterMessage] = deriveEncoder[StudentRegisterMessage]
  implicit val decoder: Decoder[StudentRegisterMessage] = deriveDecoder[StudentRegisterMessage]
}
