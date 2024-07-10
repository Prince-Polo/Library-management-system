package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.BasicInfo

case class StudentUpdateMessage(
                                 info: BasicInfo,
                                 newPassword: Option[String] = None
                               )

object StudentUpdateMessage {
  implicit val encoder: Encoder[StudentUpdateMessage] = deriveEncoder[StudentUpdateMessage]
  implicit val decoder: Decoder[StudentUpdateMessage] = deriveDecoder[StudentUpdateMessage]
}
