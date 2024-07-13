// File: APIs/StudentAPI/UpdateViolationCountMessage.scala
package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class UpdateViolationCountMessage(number: String, violationCount: String)

object UpdateViolationCountMessage {
  implicit val encoder: Encoder[UpdateViolationCountMessage] = deriveEncoder[UpdateViolationCountMessage]
  implicit val decoder: Decoder[UpdateViolationCountMessage] = deriveDecoder[UpdateViolationCountMessage]
}
