// APIs.SeatAPI.QuerySectionsByFloorMessage.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class QuerySectionsByFloorMessage(floor: String)

object QuerySectionsByFloorMessage {
  implicit val encoder: Encoder[QuerySectionsByFloorMessage] = deriveEncoder[QuerySectionsByFloorMessage]
  implicit val decoder: Decoder[QuerySectionsByFloorMessage] = deriveDecoder[QuerySectionsByFloorMessage]
}
