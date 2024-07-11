package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLoginResponse(
                                 valid: Boolean,
                                 userName: String,
                                 number: String,
                                 volunteerStatus: String,  // 改为字符串类型
                                 floor: String,            // 改为字符串类型
                                 sectionNumber: String,    // 改为字符串类型
                                 seatNumber: String,       // 改为字符串类型
                                 violationCount: String,   // 改为字符串类型
                                 volunteerHours: String    // 改为字符串类型
                               )

object StudentLoginResponse {
  implicit val encoder: Encoder[StudentLoginResponse] = deriveEncoder[StudentLoginResponse]
  implicit val decoder: Decoder[StudentLoginResponse] = deriveDecoder[StudentLoginResponse]
}
