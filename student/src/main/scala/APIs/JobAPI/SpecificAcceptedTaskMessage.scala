package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SpecificAcceptedTaskMessage(studentNumber: String) extends JobMessage[String]

object SpecificAcceptedTaskMessage {
  implicit val encoder: Encoder[SpecificAcceptedTaskMessage] = deriveEncoder[SpecificAcceptedTaskMessage]
  implicit val decoder: Decoder[SpecificAcceptedTaskMessage] = deriveDecoder[SpecificAcceptedTaskMessage]
}
