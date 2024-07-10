package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.BasicInfo

case class StudentLoginMessage(serviceName: String, `type`: String, info: BasicInfo) extends StudentMessage[String]

object StudentLoginMessage {
  implicit val encoder: Encoder[StudentLoginMessage] = deriveEncoder[StudentLoginMessage]
  implicit val decoder: Decoder[StudentLoginMessage] = deriveDecoder[StudentLoginMessage]
}
