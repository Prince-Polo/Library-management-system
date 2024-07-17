package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SubmitJobMessage(token: String, jobId: Int)

object SubmitJobMessage {
  implicit val encoder: Encoder[SubmitJobMessage] = deriveEncoder[SubmitJobMessage]
  implicit val decoder: Decoder[SubmitJobMessage] = deriveDecoder[SubmitJobMessage]
}
