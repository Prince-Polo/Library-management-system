package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class IncompleteJobByStudentQueryMessage(studentId: String)

object IncompleteJobByStudentQueryMessage {
  implicit val encoder: Encoder[IncompleteJobByStudentQueryMessage] = deriveEncoder[IncompleteJobByStudentQueryMessage]
  implicit val decoder: Decoder[IncompleteJobByStudentQueryMessage] = deriveDecoder[IncompleteJobByStudentQueryMessage]
}
