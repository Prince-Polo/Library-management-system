package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentSpecificAcceptedJobMessage(token: String)

object StudentSpecificAcceptedJobMessage {
  implicit val encoder: Encoder[StudentSpecificAcceptedJobMessage] = deriveEncoder[StudentSpecificAcceptedJobMessage]
  implicit val decoder: Decoder[StudentSpecificAcceptedJobMessage] = deriveDecoder[StudentSpecificAcceptedJobMessage]
}
