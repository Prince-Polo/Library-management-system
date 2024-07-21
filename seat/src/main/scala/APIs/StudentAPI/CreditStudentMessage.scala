package APIs.StudentAPI

import io.circe.generic.auto._  // Import generic auto

case class CreditStudentMessage(token: String, volunteerHours: String)

case class CreditStudentResponse(success: Boolean, message: String)
