package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentRegisterResponse(
                                    userName: String,
                                    volunteerStatus: String,  // 改为字符串类型
                                    floor: String,            // 改为字符串类型
                                    sectionNumber: String,    // 改为字符串类型
                                    seatNumber: String,       // 改为字符串类型
                                    violationCount: String,   // 改为字符串类型
                                    volunteerHours: String,   // 改为字符串类型
                                    token: String             // 新增字段
                                  )

object StudentRegisterResponse {
  implicit val encoder: Encoder[StudentRegisterResponse] = deriveEncoder[StudentRegisterResponse]
  implicit val decoder: Decoder[StudentRegisterResponse] = deriveDecoder[StudentRegisterResponse]
}
