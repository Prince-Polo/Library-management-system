package APIs.StudentAPI

import io.circe.generic.auto._

case class VolunteerStatusTrueMessage(number: String)
case class VolunteerStatusTrueResponse(success: Boolean, message: String)
