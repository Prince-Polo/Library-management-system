package APIs.StudentAPI

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

case class StudentSeatLeaveMessage(
                                    studentNumber: String,
                                    floor: String,
                                    section: String,
                                    seatNumber: String
                                  )

object StudentSeatLeaveMessage {
  implicit val encoder: Encoder[StudentSeatLeaveMessage] = deriveEncoder[StudentSeatLeaveMessage]
  implicit val decoder: Decoder[StudentSeatLeaveMessage] = deriveDecoder[StudentSeatLeaveMessage]
}
