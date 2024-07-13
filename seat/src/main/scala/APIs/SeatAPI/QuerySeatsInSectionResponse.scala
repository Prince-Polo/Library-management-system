// APIs.SeatAPI.QuerySeatsInSectionResponse.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import Common.SeatInfo

case class QuerySeatsInSectionResponse(totalSeats: String, freeSeats: String, seats: List[SeatInfo])

object QuerySeatsInSectionResponse {
  implicit val encoder: Encoder[QuerySeatsInSectionResponse] = deriveEncoder[QuerySeatsInSectionResponse]
  implicit val decoder: Decoder[QuerySeatsInSectionResponse] = deriveDecoder[QuerySeatsInSectionResponse]
}
