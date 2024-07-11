package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLoginResponse(
                                 valid: Boolean,
                                 userName: Option[String] = None,
                                 number: Option[String] = None,
                                 volunteerStatus: Option[String] = None,  // 改为字符串类型
                                 floor: Option[String] = None,            // 改为字符串类型
                                 sectionNumber: Option[String] = None,    // 改为字符串类型
                                 seatNumber: Option[String] = None,       // 改为字符串类型
                                 violationCount: Option[String] = None,   // 改为字符串类型
                                 volunteerHours: Option[String] = None    // 改为字符串类型
                               )

object StudentLoginResponse {
  implicit val encoder: Encoder[StudentLoginResponse] = deriveEncoder[StudentLoginResponse]
  implicit val decoder: Decoder[StudentLoginResponse] = deriveDecoder[StudentLoginResponse]
}
