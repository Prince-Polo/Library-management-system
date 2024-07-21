package Common

import io.circe.{Decoder, Encoder}

enum SeatStatus:
  case Available
  case Reported
  case Confirmed

object SeatStatus:
  given Encoder[SeatStatus] = Encoder.encodeString.contramap[SeatStatus](toString)

  given Decoder[SeatStatus] = Decoder.decodeString.emap(fromString)

  def fromString(s: String): Either[String, SeatStatus] = s match
    case "Available"  => Right(SeatStatus.Available)
    case "Reported"   => Right(SeatStatus.Reported)
    case "Confirmed"  => Right(SeatStatus.Confirmed)
    case _            => Left("Invalid SeatStatus")

  def toString(status: SeatStatus): String = status match
    case SeatStatus.Available  => "Available"
    case SeatStatus.Reported   => "Reported"
    case SeatStatus.Confirmed  => "Confirmed"
