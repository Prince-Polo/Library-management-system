package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatInfo

case class ReportedSeatQueryResponse(seats: List[SeatInfo])

object ReportedSeatQueryResponse {
  implicit val encoder: Encoder[ReportedSeatQueryResponse] = deriveEncoder[ReportedSeatQueryResponse]
  implicit val decoder: Decoder[ReportedSeatQueryResponse] = deriveDecoder[ReportedSeatQueryResponse]
}
