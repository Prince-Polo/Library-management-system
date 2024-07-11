package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentRegisterResponse(
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

object StudentRegisterResponse {
  implicit val encoder: Encoder[StudentRegisterResponse] = deriveEncoder[StudentRegisterResponse]
  implicit val decoder: Decoder[StudentRegisterResponse] = deriveDecoder[StudentRegisterResponse]
}
