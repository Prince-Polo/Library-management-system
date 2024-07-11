package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Info(
                 userName: String,
                 password: String,
                 number: String,
                 volunteerStatus: String,  // 改为字符串类型
                 floor: String,            // 改为字符串类型
                 sectionNumber: String,    // 改为字符串类型
                 seatNumber: String,       // 改为字符串类型
                 violationCount: String,   // 改为字符串类型
                 volunteerHours: String    // 改为字符串类型
               )

object Info {
  implicit val encoder: Encoder[Info] = deriveEncoder[Info]
  implicit val decoder: Decoder[Info] = deriveDecoder[Info]
}
