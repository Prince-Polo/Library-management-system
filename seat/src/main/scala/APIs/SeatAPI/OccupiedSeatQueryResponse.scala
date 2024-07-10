package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatInfo

case class OccupiedSeatQueryResponse(seats: List[SeatInfo])

object OccupiedSeatQueryResponse {
  implicit val encoder: Encoder[OccupiedSeatQueryResponse] = deriveEncoder[OccupiedSeatQueryResponse]
  implicit val decoder: Decoder[OccupiedSeatQueryResponse] = deriveDecoder[OccupiedSeatQueryResponse]
}
