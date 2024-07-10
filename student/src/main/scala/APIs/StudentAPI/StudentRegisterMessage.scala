package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.BasicInfo

case class StudentRegisterMessage(serviceName: String, `type`: String, info: BasicInfo) extends StudentMessage[String]

object StudentRegisterMessage {
  implicit val encoder: Encoder[StudentRegisterMessage] = deriveEncoder[StudentRegisterMessage]
  implicit val decoder: Decoder[StudentRegisterMessage] = deriveDecoder[StudentRegisterMessage]
}
