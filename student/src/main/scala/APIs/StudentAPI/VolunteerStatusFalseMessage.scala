package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerStatusFalseMessage(number: String)

object VolunteerStatusFalseMessage {
  implicit val encoder: Encoder[VolunteerStatusFalseMessage] = deriveEncoder[VolunteerStatusFalseMessage]
  implicit val decoder: Decoder[VolunteerStatusFalseMessage] = deriveDecoder[VolunteerStatusFalseMessage]
}
