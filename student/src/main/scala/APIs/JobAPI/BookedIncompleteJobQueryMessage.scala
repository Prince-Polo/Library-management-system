package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class BookedIncompleteJobQueryMessage()

object BookedIncompleteJobQueryMessage {
  implicit val encoder: Encoder[BookedIncompleteJobQueryMessage] = deriveEncoder[BookedIncompleteJobQueryMessage]
  implicit val decoder: Decoder[BookedIncompleteJobQueryMessage] = deriveDecoder[BookedIncompleteJobQueryMessage]
}
