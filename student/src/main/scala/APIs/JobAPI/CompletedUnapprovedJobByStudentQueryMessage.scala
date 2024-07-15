package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class CompletedUnapprovedJobByStudentQueryMessage(studentId: String)

object CompletedUnapprovedJobByStudentQueryMessage {
  implicit val encoder: Encoder[CompletedUnapprovedJobByStudentQueryMessage] = deriveEncoder[CompletedUnapprovedJobByStudentQueryMessage]
  implicit val decoder: Decoder[CompletedUnapprovedJobByStudentQueryMessage] = deriveDecoder[CompletedUnapprovedJobByStudentQueryMessage]
}
