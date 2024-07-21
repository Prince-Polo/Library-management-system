package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage

case class OccupySeatMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class OccupySeatResponse(success: Boolean, message: String)
