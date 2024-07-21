package APIs.SeatAPI

import Common.SeatInfo
import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto._

case class OccupiedSeatQueryMessage() extends SeatMessage[String]

case class OccupiedSeatQueryResponse(seats: List[SeatInfo])
