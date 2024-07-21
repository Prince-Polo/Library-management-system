// APIs.SeatAPI.SeatConfirmMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class SeatConfirmMessage(floor: String, section: String, seatNumber: String, feedback: String) extends SeatMessage[String]

case class SeatConfirmResponse(success: Boolean, message: String)
