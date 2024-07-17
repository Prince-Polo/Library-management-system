package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerStatusFalseUsingTokenResponse(success: Boolean, message: String)

object VolunteerStatusFalseUsingTokenResponse {
  implicit val encoder: Encoder[VolunteerStatusFalseUsingTokenResponse] = deriveEncoder[VolunteerStatusFalseUsingTokenResponse]
  implicit val decoder: Decoder[VolunteerStatusFalseUsingTokenResponse] = deriveDecoder[VolunteerStatusFalseUsingTokenResponse]
}
