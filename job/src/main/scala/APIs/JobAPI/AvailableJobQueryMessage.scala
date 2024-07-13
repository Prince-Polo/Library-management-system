package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AvailableJobQueryMessage()

object AvailableJobQueryMessage {
  implicit val encoder: Encoder[AvailableJobQueryMessage] = deriveEncoder[AvailableJobQueryMessage]
  implicit val decoder: Decoder[AvailableJobQueryMessage] = deriveDecoder[AvailableJobQueryMessage]
}
