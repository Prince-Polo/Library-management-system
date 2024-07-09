package APIs.PatientAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.Info

case class StudentLoginMessage(serviceName: String, `type`: String,info:Info) extends StudentMessage[String]
object StudentLoginMessage {
  implicit val encoder: Encoder[StudentLoginMessage] = deriveEncoder[StudentLoginMessage]
  implicit val decoder: Decoder[StudentLoginMessage] = deriveDecoder[StudentLoginMessage]
}