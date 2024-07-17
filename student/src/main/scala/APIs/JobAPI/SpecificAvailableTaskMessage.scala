package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SpecificAvailableTaskMessage(studentNumber: String) extends JobMessage[String]

object SpecificAvailableTaskMessage {
  implicit val encoder: Encoder[SpecificAvailableTaskMessage] = deriveEncoder[SpecificAvailableTaskMessage]
  implicit val decoder: Decoder[SpecificAvailableTaskMessage] = deriveDecoder[SpecificAvailableTaskMessage]
}
