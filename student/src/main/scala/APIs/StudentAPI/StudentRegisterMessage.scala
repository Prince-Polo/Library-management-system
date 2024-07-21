package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentRegisterMessage(userName: String, password: String, number: String)

case class StudentRegisterResponse(userName: String, volunteerStatus: Boolean, floor: String, sectionNumber: String, seatNumber: String, violationCount: Int, volunteerHours: Float, token: String)
