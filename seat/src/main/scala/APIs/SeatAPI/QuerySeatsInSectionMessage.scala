// APIs.SeatAPI.QuerySeatsInSectionMessage.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class QuerySeatsInSectionMessage(floor: String, section: String)

object QuerySeatsInSectionMessage {
  implicit val encoder: Encoder[QuerySeatsInSectionMessage] = deriveEncoder[QuerySeatsInSectionMessage]
  implicit val decoder: Decoder[QuerySeatsInSectionMessage] = deriveDecoder[QuerySeatsInSectionMessage]
}
