package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class IncompleteApprovedJobQueryResponse(jobs: List[JobInfo])

object IncompleteApprovedJobQueryResponse {
  implicit val encoder: Encoder[IncompleteApprovedJobQueryResponse] = deriveEncoder[IncompleteApprovedJobQueryResponse]
  implicit val decoder: Decoder[IncompleteApprovedJobQueryResponse] = deriveDecoder[IncompleteApprovedJobQueryResponse]
}
