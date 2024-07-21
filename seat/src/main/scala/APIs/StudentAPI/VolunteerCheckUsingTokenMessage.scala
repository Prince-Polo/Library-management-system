package APIs.StudentAPI

import io.circe.generic.auto._

case class VolunteerCheckUsingTokenMessage(token: String)
case class VolunteerCheckUsingTokenResponse(isVolunteer: Boolean, message: String)
