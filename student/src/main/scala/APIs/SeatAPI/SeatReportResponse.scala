package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatReportResponse(success: Boolean, message: String)

object SeatReportResponse {
  implicit val encoder: Encoder[SeatReportResponse] = deriveEncoder[SeatReportResponse]
  implicit val decoder: Decoder[SeatReportResponse] = deriveDecoder[SeatReportResponse]
}
