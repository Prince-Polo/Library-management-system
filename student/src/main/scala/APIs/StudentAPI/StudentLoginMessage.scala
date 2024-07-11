package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLoginMessage(
                                userName: String,
                                password: String,
                                number: String
                              ) extends StudentMessage[String]

object StudentLoginMessage {
  implicit val encoder: Encoder[StudentLoginMessage] = deriveEncoder[StudentLoginMessage]
  implicit val decoder: Decoder[StudentLoginMessage] = deriveDecoder[StudentLoginMessage]
}
