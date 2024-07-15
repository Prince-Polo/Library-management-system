package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class ApproveJobResponse(success: Boolean, message: String)

object ApproveJobResponse {
  implicit val encoder: Encoder[ApproveJobResponse] = deriveEncoder[ApproveJobResponse]
  implicit val decoder: Decoder[ApproveJobResponse] = deriveDecoder[ApproveJobResponse]
}
