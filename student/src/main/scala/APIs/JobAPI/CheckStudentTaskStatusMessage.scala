package APIs.JobAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CheckStudentTaskStatusMessage(studentId: String) extends JobMessage[String]
