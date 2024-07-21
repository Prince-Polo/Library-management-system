// APIs.SeatAPI.SeatConfirmMessage.scala
package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage
import Common.SeatInfo

case class SeatConfirmMessage(floor: String, section: String, seatNumber: String, feedback: String) extends SeatMessage[String]

case class SeatConfirmResponse(success: Boolean, message: String)
