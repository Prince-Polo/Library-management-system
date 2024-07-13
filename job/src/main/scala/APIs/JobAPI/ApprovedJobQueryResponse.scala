package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ApprovedJobQueryResponse(jobs: List[JobInfo])

object ApprovedJobQueryResponse {
  implicit val encoder: Encoder[ApprovedJobQueryResponse] = deriveEncoder[ApprovedJobQueryResponse]
  implicit val decoder: Decoder[ApprovedJobQueryResponse] = deriveDecoder[ApprovedJobQueryResponse]
}