package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto.*

case class OccupySeatMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class OccupySeatResponse(success: Boolean, message: String)
