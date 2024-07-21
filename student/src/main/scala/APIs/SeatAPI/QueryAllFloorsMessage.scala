package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.FloorInfo
import io.circe.generic.auto.*

case class QueryAllFloorsMessage() extends SeatMessage[String]

case class QueryAllFloorsResponse(floorCount: String, floors: List[FloorInfo])
