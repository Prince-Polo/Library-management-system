package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AddSeatResponse(success: Boolean, message: String) // 添加座位的响应信息

object AddSeatResponse {
  implicit val encoder: Encoder[AddSeatResponse] = deriveEncoder[AddSeatResponse]
  implicit val decoder: Decoder[AddSeatResponse] = deriveDecoder[AddSeatResponse]
}
