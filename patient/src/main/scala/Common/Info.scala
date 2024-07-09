package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Info(name: String, password: String, email: String, number: String)

object Info {
  implicit val encoder: Encoder[Info] = deriveEncoder[Info]
  implicit val decoder: Decoder[Info] = deriveDecoder[Info]
}


