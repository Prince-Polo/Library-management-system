// File: APIs/StudentAPI/UpdateViolationCountResponse.scala
package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class UpdateViolationCountResponse(success: Boolean, message: String)

object UpdateViolationCountResponse {
  implicit val encoder: Encoder[UpdateViolationCountResponse] = deriveEncoder[UpdateViolationCountResponse]
  implicit val decoder: Decoder[UpdateViolationCountResponse] = deriveDecoder[UpdateViolationCountResponse]
}
