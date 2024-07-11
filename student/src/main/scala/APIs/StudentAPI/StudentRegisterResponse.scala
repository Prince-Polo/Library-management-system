package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentRegisterResponse(
                                    userName: String,
                                    number: String,
                                    volunteerStatus: String,
                                    floor: String,
                                    sectionNumber: String,
                                    seatNumber: String,
                                    violationCount: String,
                                    volunteerHours: String
                                  )

object StudentRegisterResponse {
  implicit val encoder: Encoder[StudentRegisterResponse] = deriveEncoder[StudentRegisterResponse]
  implicit val decoder: Decoder[StudentRegisterResponse] = deriveDecoder[StudentRegisterResponse]
}
