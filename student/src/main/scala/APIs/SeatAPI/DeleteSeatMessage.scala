package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatPosition

case class DeleteSeatMessage(floor: String, section: String, seatNumber: String) // 删除座位的请求信息

object DeleteSeatMessage {
  implicit val encoder: Encoder[DeleteSeatMessage] = deriveEncoder[DeleteSeatMessage]
  implicit val decoder: Decoder[DeleteSeatMessage] = deriveDecoder[DeleteSeatMessage]
}
