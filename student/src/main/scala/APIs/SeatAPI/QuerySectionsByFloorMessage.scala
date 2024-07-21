// APIs.SeatAPI.QuerySectionsByFloorMessage.scala
package APIs.SeatAPI

import APIs.SeatAPI.SeatMessage
import Common.SectionInfo
import io.circe.generic.auto.*

case class QuerySectionsByFloorMessage(floor: String) extends SeatMessage[String]

case class QuerySectionsByFloorResponse(sectionCount: String, sections: List[SectionInfo])
