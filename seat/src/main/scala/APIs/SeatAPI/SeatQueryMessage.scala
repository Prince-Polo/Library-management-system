package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SeatQueryMessage(floor: Int, section: Int, seatNumber: Int) // 查询特定位置的座位信息

object SeatQueryMessage {
  implicit val encoder: Encoder[SeatQueryMessage] = deriveEncoder[SeatQueryMessage]
  implicit val decoder: Decoder[SeatQueryMessage] = deriveDecoder[SeatQueryMessage]
}
