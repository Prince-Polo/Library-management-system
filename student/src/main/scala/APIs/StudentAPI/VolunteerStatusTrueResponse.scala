package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerStatusTrueResponse(success: Boolean, message: String)

object VolunteerStatusTrueResponse {
  implicit val encoder: Encoder[VolunteerStatusTrueResponse] = deriveEncoder[VolunteerStatusTrueResponse]
  implicit val decoder: Decoder[VolunteerStatusTrueResponse] = deriveDecoder[VolunteerStatusTrueResponse]
}
