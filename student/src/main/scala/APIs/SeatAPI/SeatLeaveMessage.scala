// APIs.SeatAPI.SeatLeaveMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class SeatLeaveMessage(floor: String, section: String, seatNumber: String, studentNumber: String) extends SeatMessage[String]

case class SeatLeaveResponse(success: Boolean, message: String)
