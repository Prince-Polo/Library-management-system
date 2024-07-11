package APIs.JobAPI

import io.circe.Encoder
import io.circe.generic.semiauto._

case class AddJobMessage(
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


object AddJobMessage {
  implicit val encoder: Encoder[AddJobMessage] = deriveEncoder[AddJobMessage]
}