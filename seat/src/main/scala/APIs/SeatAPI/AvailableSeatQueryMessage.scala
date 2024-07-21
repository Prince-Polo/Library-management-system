package APIs.SeatAPI

import Common.SeatInfo
import io.circe.generic.auto._  // Import generic auto

case class AvailableSeatQueryMessage()

case class AvailableSeatQueryResponse(seats: List[SeatInfo])
