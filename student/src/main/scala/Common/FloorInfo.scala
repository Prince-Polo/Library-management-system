// Common.FloorInfo.scala
package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class FloorInfo(floor: String, sections: String, totalSeats: String, freeSeats: String)

object FloorInfo {
  implicit val encoder: Encoder[FloorInfo] = deriveEncoder[FloorInfo]
  implicit val decoder: Decoder[FloorInfo] = deriveDecoder[FloorInfo]
}
