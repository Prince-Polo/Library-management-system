package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class RejectedJobResponse(success: Boolean, message: String)

object RejectedJobResponse {
  implicit val encoder: Encoder[RejectedJobResponse] = deriveEncoder[RejectedJobResponse]
  implicit val decoder: Decoder[RejectedJobResponse] = deriveDecoder[RejectedJobResponse]
}
