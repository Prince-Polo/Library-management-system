package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CompletedUnapprovedJobByStudentQueryMessage(studentId: String)

object CompletedUnapprovedJobByStudentQueryMessage {
  implicit val encoder: Encoder[CompletedUnapprovedJobByStudentQueryMessage] = deriveEncoder[CompletedUnapprovedJobByStudentQueryMessage]
  implicit val decoder: Decoder[CompletedUnapprovedJobByStudentQueryMessage] = deriveDecoder[CompletedUnapprovedJobByStudentQueryMessage]
}
