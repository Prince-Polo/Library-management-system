package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentInfoUsingTokenResponse(
                                          userName: String,
                                          volunteerStatus: String,
                                          floor: String,
                                          sectionNumber: String,
                                          seatNumber: String,
                                          violationCount: String,
                                          volunteerHours: String
                                        )

object StudentInfoUsingTokenResponse {
  implicit val encoder: Encoder[StudentInfoUsingTokenResponse] = deriveEncoder[StudentInfoUsingTokenResponse]
  implicit val decoder: Decoder[StudentInfoUsingTokenResponse] = deriveDecoder[StudentInfoUsingTokenResponse]
}
