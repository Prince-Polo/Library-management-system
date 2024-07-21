package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentSeatLeaveMessage(token: String, floor: String, section: String, seatNumber: String)

case class StudentSeatLeaveResponse(success: Boolean, message: String)
