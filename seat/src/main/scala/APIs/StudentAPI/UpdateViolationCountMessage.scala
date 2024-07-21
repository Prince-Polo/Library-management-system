package APIs.StudentAPI

import io.circe.generic.auto._

case class UpdateViolationCountMessage(number: String, violationCount: Int)

case class UpdateViolationCountResponse(success: Boolean, message: String)
