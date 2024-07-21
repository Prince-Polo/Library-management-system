package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentLeaveMessage(studentNumber: String, floor: String, section: String, seatNumber: String) extends StudentMessage[String]
case class StudentLeaveResponse(success: Boolean, message: String)
