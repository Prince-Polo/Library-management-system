// APIs/JobAPI.scala
package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
case class ForceEndTaskMessage(jobId: Int, studentId: String)

object ForceEndTaskMessage {
  implicit val encoder: Encoder[ForceEndTaskMessage] = deriveEncoder[ForceEndTaskMessage]
  implicit val decoder: Decoder[ForceEndTaskMessage] = deriveDecoder[ForceEndTaskMessage]
}