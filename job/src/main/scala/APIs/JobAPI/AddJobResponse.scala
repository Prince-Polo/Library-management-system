package APIs.JobAPI

import io.circe.Encoder
import io.circe.generic.semiauto._

case class AddJobResponse(success: Boolean, message: String)

object AddJobResponse {
  implicit val encoder: Encoder[AddJobResponse] = deriveEncoder[AddJobResponse]
}