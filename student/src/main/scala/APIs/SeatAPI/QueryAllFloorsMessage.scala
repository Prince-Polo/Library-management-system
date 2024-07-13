// APIs.SeatAPI.QueryAllFloorsMessage.scala
package APIs.SeatAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class QueryAllFloorsMessage()

object QueryAllFloorsMessage {
  implicit val encoder: Encoder[QueryAllFloorsMessage] = deriveEncoder[QueryAllFloorsMessage]
  implicit val decoder: Decoder[QueryAllFloorsMessage] = deriveDecoder[QueryAllFloorsMessage]
}
