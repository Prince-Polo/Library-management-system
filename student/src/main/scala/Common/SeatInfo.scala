package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatInfo(
                     floor: String,
                     section: String,
                     seatNumber: String,
                     status: String, // 使用字符串类型
                     feedback: String,
                     occupied: String, // 使用字符串类型
                     studentNumber: String
                   )

object SeatInfo {
  implicit val encoder: Encoder[SeatInfo] = deriveEncoder[SeatInfo]
  implicit val decoder: Decoder[SeatInfo] = deriveDecoder[SeatInfo]
}
