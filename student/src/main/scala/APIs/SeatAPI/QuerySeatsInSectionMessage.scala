package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class QuerySeatsInSectionMessage(floor: String, section: String) extends SeatMessage[String]

case class QuerySeatsInSectionResponse(totalSeats: String, freeSeats: String, seats: List[SeatInfo])
