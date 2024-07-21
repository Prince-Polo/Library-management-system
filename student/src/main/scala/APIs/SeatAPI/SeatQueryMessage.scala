// APIs.SeatAPI.SeatQueryMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class SeatQueryMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class SeatQueryResponse(seat: Option[SeatInfo])
