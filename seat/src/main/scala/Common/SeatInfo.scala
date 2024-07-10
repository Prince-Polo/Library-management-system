package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatInfo(
                     floor: Int,
                     section: Int,
                     seatNumber: Int,
                     status: String,
                     feedback: String,
                     occupied: Boolean,
                     studentNumber: String
                   )

object SeatInfo {
  implicit val encoder: Encoder[SeatInfo] = deriveEncoder[SeatInfo]
  implicit val decoder: Decoder[SeatInfo] = deriveDecoder[SeatInfo]
}
