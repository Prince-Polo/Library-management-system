package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatInfo

case class AvailableSeatQueryResponse(seats: List[SeatInfo])

object AvailableSeatQueryResponse {
  implicit val encoder: Encoder[AvailableSeatQueryResponse] = deriveEncoder[AvailableSeatQueryResponse]
  implicit val decoder: Decoder[AvailableSeatQueryResponse] = deriveDecoder[AvailableSeatQueryResponse]
}
