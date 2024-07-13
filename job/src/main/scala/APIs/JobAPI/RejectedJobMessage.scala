package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.generic.auto._
import io.circe.syntax._

case class RejectedJobMessage(                          jobId: BigInt,
                                                        jobStudentId: String,
                                                        jobShortDescription: String,
                                                        jobLongDescription: String,
                                                        jobHardness: String,
                                                        jobCredit: String,
                                                        jobComplete: String,
                                                        jobBooked: String,
                                                        jobApproved: String
                             )

object RejectedJobMessage{
  implicit val encoder: Encoder[RejectedJobMessage] = deriveEncoder[RejectedJobMessage]
  implicit val decoder: Decoder[RejectedJobMessage] = deriveDecoder[RejectedJobMessage]
}