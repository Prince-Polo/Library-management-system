package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SubmitJobResponse(success: Boolean, message: String)

object SubmitJobResponse {
  implicit val encoder: Encoder[SubmitJobResponse] = deriveEncoder[SubmitJobResponse]
  implicit val decoder: Decoder[SubmitJobResponse] = deriveDecoder[SubmitJobResponse]
}
