package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class JobInfo(
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

object JobInfo {
  implicit val encoder: Encoder[JobInfo] = deriveEncoder[JobInfo]
  implicit val decoder: Decoder[JobInfo] = deriveDecoder[JobInfo]
}

