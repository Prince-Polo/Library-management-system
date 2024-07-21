package APIs.SeatAPI

import Common.SeatInfo
import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto._

case class ConfirmedSeatQueryMessage() extends SeatMessage[String]

case class ConfirmedSeatQueryResponse(seats: List[SeatInfo])
