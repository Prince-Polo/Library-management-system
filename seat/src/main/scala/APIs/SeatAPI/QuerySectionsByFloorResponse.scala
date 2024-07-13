// APIs.SeatAPI.QuerySectionsByFloorResponse.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SectionInfo

case class QuerySectionsByFloorResponse(sectionCount: String, sections: List[SectionInfo])

object QuerySectionsByFloorResponse {
  implicit val encoder: Encoder[QuerySectionsByFloorResponse] = deriveEncoder[QuerySectionsByFloorResponse]
  implicit val decoder: Decoder[QuerySectionsByFloorResponse] = deriveDecoder[QuerySectionsByFloorResponse]
}
