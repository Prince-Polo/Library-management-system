package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentUpdateMessage(userName: String, password: String, number: String, newPassword: Option[String])

case class StudentUpdateResponse(success: Boolean, message: String)
