package APIs.StudentAPI

import io.circe.generic.auto._

case class UpdateVolunteerHourMessage(jobId: Int, studentId: String)

case class UpdateVolunteerHourResponse(success: Boolean, message: String)
