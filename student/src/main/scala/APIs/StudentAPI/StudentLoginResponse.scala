package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLoginResponse(
                                 valid: Boolean,
                                 id: Option[Int] = None,
                                 authority: Option[Int] = None
                               )

object StudentLoginResponse {
  implicit val encoder: Encoder[StudentLoginResponse] = deriveEncoder[StudentLoginResponse]
  implicit val decoder: Decoder[StudentLoginResponse] = deriveDecoder[StudentLoginResponse]
}
