// APIs.SeatAPI.QueryAllFloorsResponse.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.FloorInfo

case class QueryAllFloorsResponse(floorCount: String, floors: List[FloorInfo])

object QueryAllFloorsResponse {
  implicit val encoder: Encoder[QueryAllFloorsResponse] = deriveEncoder[QueryAllFloorsResponse]
  implicit val decoder: Decoder[QueryAllFloorsResponse] = deriveDecoder[QueryAllFloorsResponse]
}
