package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatInfo

case class SeatQueryResponse(seat: Option[SeatInfo])

object SeatQueryResponse {
  implicit val encoder: Encoder[SeatQueryResponse] = deriveEncoder[SeatQueryResponse]
  implicit val decoder: Decoder[SeatQueryResponse] = deriveDecoder[SeatQueryResponse]
}
