package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class OccupiedSeatQueryMessage() extends SeatMessage[String]

case class OccupiedSeatQueryResponse(seats: List[SeatInfo])
