package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatReportMessage(floor: String, section: String, seatNumber: String, feedback: String)

object SeatReportMessage {
  implicit val encoder: Encoder[SeatReportMessage] = deriveEncoder[SeatReportMessage]
  implicit val decoder: Decoder[SeatReportMessage] = deriveDecoder[SeatReportMessage]
}
