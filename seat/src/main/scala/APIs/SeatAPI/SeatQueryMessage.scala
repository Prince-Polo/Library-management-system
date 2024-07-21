// APIs.SeatAPI.SeatQueryMessage.scala
package APIs.SeatAPI

import io.circe.generic.auto._
import APIs.SeatAPI.SeatMessage
import Common.SeatInfo

case class SeatQueryMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class SeatQueryResponse(seat: Option[SeatInfo])
