package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class BookedCompletedUnapprovedJobQueryMessage()

object BookedCompletedUnapprovedJobQueryMessage {
  implicit val encoder: Encoder[BookedCompletedUnapprovedJobQueryMessage] = deriveEncoder[BookedCompletedUnapprovedJobQueryMessage]
  implicit val decoder: Decoder[BookedCompletedUnapprovedJobQueryMessage] = deriveDecoder[BookedCompletedUnapprovedJobQueryMessage]
}
