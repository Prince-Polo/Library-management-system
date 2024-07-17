package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerStatusTrueMessage(number: String)

object VolunteerStatusTrueMessage {
  implicit val encoder: Encoder[VolunteerStatusTrueMessage] = deriveEncoder[VolunteerStatusTrueMessage]
  implicit val decoder: Decoder[VolunteerStatusTrueMessage] = deriveDecoder[VolunteerStatusTrueMessage]
}
