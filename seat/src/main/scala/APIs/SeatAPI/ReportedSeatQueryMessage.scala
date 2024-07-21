// APIs.SeatAPI.ReportedSeatQueryMessage.scala
package APIs.SeatAPI

import Common.SeatInfo
import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto._

case class ReportedSeatQueryMessage() extends SeatMessage[String]

case class ReportedSeatQueryResponse(seats: List[SeatInfo])
