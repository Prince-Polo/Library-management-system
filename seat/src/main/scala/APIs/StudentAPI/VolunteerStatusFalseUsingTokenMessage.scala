package APIs.StudentAPI

import io.circe.generic.auto._ // Import Circe auto derivation

case class VolunteerStatusFalseUsingTokenMessage(token: String)
case class VolunteerStatusFalseUsingTokenResponse(success: Boolean, message: String)
