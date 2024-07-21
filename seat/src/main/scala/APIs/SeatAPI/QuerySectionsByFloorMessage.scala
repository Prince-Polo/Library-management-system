// APIs.SeatAPI.QuerySectionsByFloorMessage.scala
package APIs.SeatAPI

import Common.SectionInfo
import APIs.SeatAPI.SeatMessage
import io.circe.generic.auto._

case class QuerySectionsByFloorMessage(floor: String) extends SeatMessage[String]

case class QuerySectionsByFloorResponse(sectionCount: String, sections: List[SectionInfo])
