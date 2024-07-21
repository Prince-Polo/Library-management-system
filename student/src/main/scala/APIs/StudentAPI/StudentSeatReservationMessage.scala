package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentSeatReservationMessage(token: String, floor: String, section: String, seatNumber: String)

case class StudentSeatReservationResponse(success: Boolean, message: String)
