package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AcceptJobResponse(success: Boolean, message: String)

object AcceptJobResponse {
  implicit val encoder: Encoder[AcceptJobResponse] = deriveEncoder[AcceptJobResponse]
  implicit val decoder: Decoder[AcceptJobResponse] = deriveDecoder[AcceptJobResponse]
}
