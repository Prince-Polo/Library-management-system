package APIs.StudentAPI

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class StudentSeatReservationMessage(
                                          studentNumber: String,
                                          floor: String,
                                          section: String,
                                          seatNumber: String
                                        )

object StudentSeatReservationMessage {
  implicit val encoder: Encoder[StudentSeatReservationMessage] = deriveEncoder[StudentSeatReservationMessage]
  implicit val decoder: Decoder[StudentSeatReservationMessage] = deriveDecoder[StudentSeatReservationMessage]
}
