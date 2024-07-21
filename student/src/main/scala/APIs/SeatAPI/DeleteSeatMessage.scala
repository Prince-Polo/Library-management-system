package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto.*

case class DeleteSeatMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class DeleteSeatResponse(success: Boolean, message: String)
