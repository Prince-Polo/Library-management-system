package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentInfoUsingTokenMessage(token: String)

case class StudentInfoUsingTokenResponse(
                                          userName: String,
                                          volunteerStatus: Boolean,
                                          floor: String,
                                          sectionNumber: String,
                                          seatNumber: String,
                                          violationCount: Int,       // 更改为 Int 类型
                                          volunteerHours: Float      // 更改为 Float 类型
                                        )
