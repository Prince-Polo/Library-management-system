package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatInfo(floor: Int, section: Int, seatNumber: Int)

case class SeatAvailableResponse(seats: List[SeatInfo])

object SeatAvailableResponse {
  implicit val encoder: Encoder[SeatAvailableResponse] = deriveEncoder[SeatAvailableResponse]
  implicit val decoder: Decoder[SeatAvailableResponse] = deriveDecoder[SeatAvailableResponse]
}

object SeatInfo {
  implicit val encoder: Encoder[SeatInfo] = deriveEncoder[SeatInfo]
  implicit val decoder: Decoder[SeatInfo] = deriveDecoder[SeatInfo]
}
