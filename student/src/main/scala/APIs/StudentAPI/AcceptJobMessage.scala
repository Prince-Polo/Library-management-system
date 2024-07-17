package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AcceptJobMessage(token: String, jobId: Int)

object AcceptJobMessage {
  implicit val encoder: Encoder[AcceptJobMessage] = deriveEncoder[AcceptJobMessage]
  implicit val decoder: Decoder[AcceptJobMessage] = deriveDecoder[AcceptJobMessage]
}
