package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentInfoResponse(
                                userName: String,
                                number: String,
                                volunteerStatus: String,  // 改为字符串类型
                                floor: String,            // 改为字符串类型
                                sectionNumber: String,    // 改为字符串类型
                                seatNumber: String,       // 改为字符串类型
                                violationCount: String,   // 改为字符串类型
                                volunteerHours: String    // 改为字符串类型
                              )

object StudentInfoResponse {
  implicit val encoder: Encoder[StudentInfoResponse] = deriveEncoder[StudentInfoResponse]
  implicit val decoder: Decoder[StudentInfoResponse] = deriveDecoder[StudentInfoResponse]
}
