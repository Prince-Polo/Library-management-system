package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerCheckUsingTokenResponse(isVolunteer: Boolean, message: String)

object VolunteerCheckUsingTokenResponse {
  implicit val encoder: Encoder[VolunteerCheckUsingTokenResponse] = deriveEncoder[VolunteerCheckUsingTokenResponse]
  implicit val decoder: Decoder[VolunteerCheckUsingTokenResponse] = deriveDecoder[VolunteerCheckUsingTokenResponse]
}
