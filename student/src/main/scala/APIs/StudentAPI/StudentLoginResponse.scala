package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentLoginResponse(
                                 valid: Boolean,
                                 userName: Option[String] = None,
                                 number: Option[String] = None,
                                 volunteerStatus: Option[Boolean] = None,
                                 floor: Option[Int] = None,
                                 sectionNumber: Option[Int] = None,
                                 seatNumber: Option[Int] = None,
                                 violationCount: Option[Int] = None,
                                 volunteerHours: Option[Int] = None,
                                 completedTaskIds: Option[List[Int]] = None
                               )

object StudentLoginResponse {
  implicit val encoder: Encoder[StudentLoginResponse] = deriveEncoder[StudentLoginResponse]
  implicit val decoder: Decoder[StudentLoginResponse] = deriveDecoder[StudentLoginResponse]
}
