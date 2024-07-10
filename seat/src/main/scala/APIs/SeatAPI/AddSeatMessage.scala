package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatPosition

case class AddSeatMessage(position: SeatPosition) // 添加座位的请求信息

object AddSeatMessage {
  implicit val encoder: Encoder[AddSeatMessage] = deriveEncoder[AddSeatMessage]
  implicit val decoder: Decoder[AddSeatMessage] = deriveDecoder[AddSeatMessage]
}
