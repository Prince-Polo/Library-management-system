package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerCheckUsingTokenMessage(token: String)

object VolunteerCheckUsingTokenMessage {
  implicit val encoder: Encoder[VolunteerCheckUsingTokenMessage] = deriveEncoder[VolunteerCheckUsingTokenMessage]
  implicit val decoder: Decoder[VolunteerCheckUsingTokenMessage] = deriveDecoder[VolunteerCheckUsingTokenMessage]
}
