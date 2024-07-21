// APIs.SeatAPI.SeatReservationMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto.*

case class SeatReservationMessage(floor: String, section: String, seatNumber: String, studentNumber: String) extends SeatMessage[String]

case class SeatReservationResponse(success: Boolean, message: String)
