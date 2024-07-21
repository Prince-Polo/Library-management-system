package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage

case class DeleteSeatMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class DeleteSeatResponse(success: Boolean, message: String)
