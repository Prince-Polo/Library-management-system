package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ReportedSeatQueryMessage()

object ReportedSeatQueryMessage {
  implicit val encoder: Encoder[ReportedSeatQueryMessage] = deriveEncoder[ReportedSeatQueryMessage]
  implicit val decoder: Decoder[ReportedSeatQueryMessage] = deriveDecoder[ReportedSeatQueryMessage]
}
