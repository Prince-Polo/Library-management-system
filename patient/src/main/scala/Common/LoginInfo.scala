package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class LoginInfo(name: String, password: String)
object LoginInfo {
  implicit val encoder: Encoder[Info] = deriveEncoder[Info]
  implicit val decoder: Decoder[Info] = deriveDecoder[Info]
}