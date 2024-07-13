package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class IncompleteApprovedJobQueryMessage()

object IncompleteApprovedJobQueryMessage {
  implicit val encoder: Encoder[IncompleteApprovedJobQueryMessage] = deriveEncoder[IncompleteApprovedJobQueryMessage]
  implicit val decoder: Decoder[IncompleteApprovedJobQueryMessage] = deriveDecoder[IncompleteApprovedJobQueryMessage]
}
