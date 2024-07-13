package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.*

case class AddJobResponse(success: Boolean, message: String)

object AddJobResponse {
  implicit val encoder: Encoder[AddJobResponse] = deriveEncoder[AddJobResponse]
  implicit val decoder: Decoder[AddJobResponse] = deriveDecoder[AddJobResponse]
}