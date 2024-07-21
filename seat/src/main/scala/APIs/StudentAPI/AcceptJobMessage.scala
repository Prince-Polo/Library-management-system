package APIs.StudentAPI

import io.circe.generic.auto._  // Import generic auto

case class AcceptJobMessage(token: String, jobId: Int)

case class AcceptJobResponse(success: Boolean, message: String)
