package APIs.SeatAPI

import io.circe.generic.auto.*

case class AddSeatMessage(floor: String, section: String, seatNumber: String)
case class AddSeatResponse(success: Boolean, message: String)
