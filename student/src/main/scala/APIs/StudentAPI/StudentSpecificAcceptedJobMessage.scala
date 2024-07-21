package APIs.StudentAPI

import io.circe.generic.auto._
import APIs.JobAPI.TaskInfo

case class StudentSpecificAcceptedJobMessage(token: String)

case class StudentSpecificAcceptedJobResponse(success: Boolean, message: String, tasks: Option[List[TaskInfo]])
