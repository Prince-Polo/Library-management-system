package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerStatusFalseResponse(success: Boolean, message: String)

object VolunteerStatusFalseResponse {
  implicit val encoder: Encoder[VolunteerStatusFalseResponse] = deriveEncoder[VolunteerStatusFalseResponse]
  implicit val decoder: Decoder[VolunteerStatusFalseResponse] = deriveDecoder[VolunteerStatusFalseResponse]
}
