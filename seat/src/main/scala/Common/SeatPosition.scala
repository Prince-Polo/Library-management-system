package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatPosition(floor: Int, section: Int, seatNumber: Int)

object SeatPosition {
  implicit val encoder: Encoder[SeatPosition] = deriveEncoder[SeatPosition]
  implicit val decoder: Decoder[SeatPosition] = deriveDecoder[SeatPosition]
}
