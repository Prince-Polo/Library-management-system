package Common

import io.circe.{Decoder, Encoder, HCursor, Json}

object SeatStatus extends Enumeration {
  type SeatStatus = Value
  val Normal, Reported, Confirmed = Value

  implicit val encoder: Encoder[SeatStatus] = new Encoder[SeatStatus] {
    final def apply(a: SeatStatus): Json = Json.fromString(a.toString)
  }

  implicit val decoder: Decoder[SeatStatus] = new Decoder[SeatStatus] {
    final def apply(c: HCursor): Decoder.Result[SeatStatus] = c.as[String].map(SeatStatus.withName)
  }
}
