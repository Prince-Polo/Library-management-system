package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatLeaveResponse(success: Boolean, message: String)

object SeatLeaveResponse {
  implicit val encoder: Encoder[SeatLeaveResponse] = deriveEncoder[SeatLeaveResponse]
  implicit val decoder: Decoder[SeatLeaveResponse] = deriveDecoder[SeatLeaveResponse]
}
