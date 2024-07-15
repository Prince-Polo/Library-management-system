package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class ApprovedJobQueryMessage()

object ApprovedJobQueryMessage {
  implicit val encoder: Encoder[ApprovedJobQueryMessage] = deriveEncoder[ApprovedJobQueryMessage]
  implicit val decoder: Decoder[ApprovedJobQueryMessage] = deriveDecoder[ApprovedJobQueryMessage]
}
