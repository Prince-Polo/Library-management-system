package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class BasicInfo(
                      userName: String,
                      password: String,
                      number: String
                    )

object BasicInfo {
  implicit val encoder: Encoder[BasicInfo] = deriveEncoder[BasicInfo]
  implicit val decoder: Decoder[BasicInfo] = deriveDecoder[BasicInfo]
}
