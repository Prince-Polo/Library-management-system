package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class AvailableJobQueryMessage()

object AvailableJobQueryMessage {
  implicit val encoder: Encoder[AvailableJobQueryMessage] = deriveEncoder[AvailableJobQueryMessage]
  implicit val decoder: Decoder[AvailableJobQueryMessage] = deriveDecoder[AvailableJobQueryMessage]
}
