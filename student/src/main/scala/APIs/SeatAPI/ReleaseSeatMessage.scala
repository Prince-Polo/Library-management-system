// APIs.SeatAPI.ReleaseSeatMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto.*

case class ReleaseSeatMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class ReleaseSeatResponse(success: Boolean, message: String)
