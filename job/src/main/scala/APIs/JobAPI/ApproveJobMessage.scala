package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ApproveJobMessage(
                              jobId: BigInt,
                              jobStudentId: String,
                              jobShortDescription: String,
                              jobLongDescription: String,
                              jobHardness: String,
                              jobCredit: String,
                              jobComplete: String,
                              jobBooked: String,
                              jobApproved: String
                            )

object ApproveJobMessage {
  implicit val encoder: Encoder[ApproveJobMessage] = deriveEncoder[ApproveJobMessage]
  implicit val decoder: Decoder[ApproveJobMessage] = deriveDecoder[ApproveJobMessage]
}
