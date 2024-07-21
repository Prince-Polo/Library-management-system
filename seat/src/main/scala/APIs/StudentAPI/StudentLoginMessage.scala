package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentLoginMessage(userName: String, password: String, number: String)

case class StudentLoginResponse(valid: Boolean, userName: String, token: String, volunteerStatus: Boolean, floor: String, sectionNumber: String, seatNumber: String, violationCount: Int, volunteerHours: Float)
