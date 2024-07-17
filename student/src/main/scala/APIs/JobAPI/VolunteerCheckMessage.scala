package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class VolunteerCheckMessage(token: String) extends JobMessage[String]
