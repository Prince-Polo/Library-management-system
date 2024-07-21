package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentInfoMessage(token: String)

case class StudentInfoResponse(
                                userName: String,
                                number: String,
                                volunteerStatus: Boolean,
                                floor: String,
                                sectionNumber: String,
                                seatNumber: String,
                                violationCount: Int,       // 更改为 Int 类型
                                volunteerHours: Float      // 更改为 Float 类型
                              )
