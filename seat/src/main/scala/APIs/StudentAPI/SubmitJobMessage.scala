package APIs.StudentAPI

import io.circe.generic.auto._

case class SubmitJobMessage(token: String, jobId: Int)

case class SubmitJobResponse(success: Boolean, message: String)
