// APIs.SeatAPI.FloorQueryMessage.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class FloorQueryMessage()

object FloorQueryMessage {
  implicit val encoder: Encoder[FloorQueryMessage] = deriveEncoder[FloorQueryMessage]
  implicit val decoder: Decoder[FloorQueryMessage] = deriveDecoder[FloorQueryMessage]
}
