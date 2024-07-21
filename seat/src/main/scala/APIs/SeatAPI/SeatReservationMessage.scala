// APIs.SeatAPI.SeatReservationMessage.scala
package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage

case class SeatReservationMessage(floor: String, section: String, seatNumber: String, studentNumber: String) extends SeatMessage[String]

case class SeatReservationResponse(success: Boolean, message: String)
