package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class BookedIncompleteJobQueryMessage()

object BookedIncompleteJobQueryMessage {
  implicit val encoder: Encoder[BookedIncompleteJobQueryMessage] = deriveEncoder[BookedIncompleteJobQueryMessage]
  implicit val decoder: Decoder[BookedIncompleteJobQueryMessage] = deriveDecoder[BookedIncompleteJobQueryMessage]
}
