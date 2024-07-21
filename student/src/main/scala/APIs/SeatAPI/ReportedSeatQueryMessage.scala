// APIs.SeatAPI.ReportedSeatQueryMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SeatInfo
import io.circe.generic.auto.*

case class ReportedSeatQueryMessage() extends SeatMessage[String]

case class ReportedSeatQueryResponse(seats: List[SeatInfo])
