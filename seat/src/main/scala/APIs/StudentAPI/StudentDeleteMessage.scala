package APIs.StudentAPI

import io.circe.generic.auto._

case class StudentDeleteMessage(token: String, password: String)
case class StudentDeleteResponse(success: Boolean, message: String)
