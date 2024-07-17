package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._

case class FetchTasksMessage(studentId: String)
case class TaskInfo(jobId: Int, studentId: String, status: Int)
case class FetchTasksResponse(tasks: List[TaskInfo])

