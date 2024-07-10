package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class DeleteSeatResponse(success: Boolean, message: String) // 删除座位的响应信息

object DeleteSeatResponse {
  implicit val encoder: Encoder[DeleteSeatResponse] = deriveEncoder[DeleteSeatResponse]
  implicit val decoder: Decoder[DeleteSeatResponse] = deriveDecoder[DeleteSeatResponse]
}
