// APIs.SeatAPI.SeatLeaveMessage.scala
package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage
import Common.SeatInfo

case class SeatLeaveMessage(floor: String, section: String, seatNumber: String, studentNumber: String) extends SeatMessage[String]

case class SeatLeaveResponse(success: Boolean, message: String)
