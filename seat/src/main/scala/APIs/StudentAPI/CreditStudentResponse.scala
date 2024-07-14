package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CreditStudentResponse(success: Boolean, message: String)

object CreditStudentResponse {
  implicit val encoder: Encoder[CreditStudentResponse] = deriveEncoder[CreditStudentResponse]
  implicit val decoder: Decoder[CreditStudentResponse] = deriveDecoder[CreditStudentResponse]
}
