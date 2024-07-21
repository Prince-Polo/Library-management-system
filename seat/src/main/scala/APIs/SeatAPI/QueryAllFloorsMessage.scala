package APIs.SeatAPI

import Common.FloorInfo
import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto._

case class QueryAllFloorsMessage() extends SeatMessage[String]

case class QueryAllFloorsResponse(floorCount: String, floors: List[FloorInfo])
