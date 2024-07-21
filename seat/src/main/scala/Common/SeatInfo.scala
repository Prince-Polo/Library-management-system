package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatInfo(
                     floor: String,
                     section: String,
                     seatNumber: String,
                     status: SeatStatus,
                     feedback: String,
                     occupied: Boolean,
                     studentNumber: String
                   )

object SeatInfo {
  implicit val encoder: Encoder[SeatInfo] = deriveEncoder[SeatInfo]
  implicit val decoder: Decoder[SeatInfo] = deriveDecoder[SeatInfo]
}
