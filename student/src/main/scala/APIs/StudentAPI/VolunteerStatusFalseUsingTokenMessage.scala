package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerStatusFalseUsingTokenMessage(token: String)

object VolunteerStatusFalseUsingTokenMessage {
  implicit val encoder: Encoder[VolunteerStatusFalseUsingTokenMessage] = deriveEncoder[VolunteerStatusFalseUsingTokenMessage]
  implicit val decoder: Decoder[VolunteerStatusFalseUsingTokenMessage] = deriveDecoder[VolunteerStatusFalseUsingTokenMessage]
}
