package APIs.JobAPI

import APIs.JobAPI.JobMessage

case class CreateTaskMessage(jobId: Int, studentId: String) extends JobMessage[String]