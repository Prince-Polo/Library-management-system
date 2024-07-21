package APIs.SeatAPI

import Common.SeatInfo
import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto._

case class QuerySeatsInSectionMessage(floor: String, section: String) extends SeatMessage[String]

case class QuerySeatsInSectionResponse(totalSeats: String, freeSeats: String, seats: List[SeatInfo])
