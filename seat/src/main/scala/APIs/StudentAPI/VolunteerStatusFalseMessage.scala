package APIs.StudentAPI

import io.circe.generic.auto._

case class VolunteerStatusFalseMessage(number: String)

case class VolunteerStatusFalseResponse(success: Boolean, message: String)
