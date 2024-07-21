// APIs.SeatAPI.SeatReportMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto.*

case class SeatReportMessage(floor: String, section: String, seatNumber: String, feedback: String) extends SeatMessage[String]

case class SeatReportResponse(success: Boolean, message: String)
