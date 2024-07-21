package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentReservationMessage(studentNumber: String, floor: String, section: String, seatNumber: String) extends StudentMessage[String]

case class StudentReservationResponse(success: Boolean, message: String)
