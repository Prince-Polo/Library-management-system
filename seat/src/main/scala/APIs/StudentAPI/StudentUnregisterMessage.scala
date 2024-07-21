package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentUnregisterMessage(number: String)

case class StudentUnregisterResponse(success: Boolean, message: String)
