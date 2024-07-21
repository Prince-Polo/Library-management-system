package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class ConfirmedSeatQueryMessage() extends SeatMessage[String]

case class ConfirmedSeatQueryResponse(seats: List[SeatInfo])
