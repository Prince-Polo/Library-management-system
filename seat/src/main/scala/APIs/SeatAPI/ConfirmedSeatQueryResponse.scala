package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatInfo

case class ConfirmedSeatQueryResponse(seats: List[SeatInfo])

object ConfirmedSeatQueryResponse {
  implicit val encoder: Encoder[ConfirmedSeatQueryResponse] = deriveEncoder[ConfirmedSeatQueryResponse]
  implicit val decoder: Decoder[ConfirmedSeatQueryResponse] = deriveDecoder[ConfirmedSeatQueryResponse]
}
