package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CreditJobResponse(success: Boolean, message: String)

object CreditJobResponse {
  implicit val encoder: Encoder[CreditJobResponse] = deriveEncoder[CreditJobResponse]
  implicit val decoder: Decoder[CreditJobResponse] = deriveDecoder[CreditJobResponse]
}
