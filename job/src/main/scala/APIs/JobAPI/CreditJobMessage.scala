package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CreditJobMessage(
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

object CreditJobMessage {
  implicit val encoder: Encoder[CreditJobMessage] = deriveEncoder[CreditJobMessage]
  implicit val decoder: Decoder[CreditJobMessage] = deriveDecoder[CreditJobMessage]
}
