package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Info(
                 userName: String,
                 password: String,
                 number: String,
                 volunteerStatus: Boolean,
                 buildingNumber: Int,
                 sectionNumber: Int,
                 seatNumber: Int,
                 violationCount: Int,
                 volunteerHours: Int,
                 completedTaskIds: List[Int]
               )

object Info {
  implicit val encoder: Encoder[Info] = deriveEncoder[Info]
  implicit val decoder: Decoder[Info] = deriveDecoder[Info]
}
