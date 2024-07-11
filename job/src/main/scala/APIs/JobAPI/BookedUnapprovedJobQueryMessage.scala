package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class BookedUnapprovedJobQueryMessage()

object BookedUnapprovedJobQueryMessage {
  implicit val encoder: Encoder[BookedUnapprovedJobQueryMessage] = deriveEncoder[BookedUnapprovedJobQueryMessage]
  implicit val decoder: Decoder[BookedUnapprovedJobQueryMessage] = deriveDecoder[BookedUnapprovedJobQueryMessage]
}
