// APIs.SeatAPI.SeatRefreshMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import io.circe.Encoder
import io.circe.generic.auto.*

case class SeatRefreshMessage(floor: String, section: String, seatNumber: String) extends SeatMessage[String]

case class SeatRefreshResponse(success: Boolean, message: String)
