// APIs.SeatAPI.SeatReportMessage.scala
package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage

case class SeatReportMessage(floor: String, section: String, seatNumber: String, feedback: String) extends SeatMessage[String]

case class SeatReportResponse(success: Boolean, message: String)
