package APIs.JobAPI

import APIs.JobAPI.JobMessage

case class SubmitTaskMessage(jobId: Int, studentId: String) extends JobMessage[String]