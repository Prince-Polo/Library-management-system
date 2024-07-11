package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object SeatStatus extends Enumeration {
  type SeatStatus = Value
  val Normal, Reported, Confirmed = Value

  implicit val encoder: Encoder[SeatStatus] = Encoder.encodeString.contramap(_.toString)
  implicit val decoder: Decoder[SeatStatus] = Decoder.decodeString.map(SeatStatus.withName)
}
