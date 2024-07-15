package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentSeatLeaveMessage(
                                    token: String,
                                    floor: String,
                                    section: String,
                                    seatNumber: String
                                  )

object StudentSeatLeaveMessage {
  implicit val encoder: Encoder[StudentSeatLeaveMessage] = deriveEncoder[StudentSeatLeaveMessage]
  implicit val decoder: Decoder[StudentSeatLeaveMessage] = deriveDecoder[StudentSeatLeaveMessage]
}
