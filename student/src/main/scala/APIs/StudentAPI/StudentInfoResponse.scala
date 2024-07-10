package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentInfoResponse(
                                userName: String,
                                number: String,
                                volunteerStatus: Boolean,
                                floor: Int,
                                sectionNumber: Int,
                                seatNumber: Int,
                                violationCount: Int,
                                volunteerHours: Int,
                                completedTaskIds: List[Int]
                              )

object StudentInfoResponse {
  implicit val encoder: Encoder[StudentInfoResponse] = deriveEncoder[StudentInfoResponse]
  implicit val decoder: Decoder[StudentInfoResponse] = deriveDecoder[StudentInfoResponse]
}
