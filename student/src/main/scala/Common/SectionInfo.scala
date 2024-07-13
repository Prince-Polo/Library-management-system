// Common.SectionInfo.scala
package Common

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class SectionInfo(section: String, totalSeats: String, freeSeats: String)

object SectionInfo {
  implicit val encoder: Encoder[SectionInfo] = deriveEncoder[SectionInfo]
  implicit val decoder: Decoder[SectionInfo] = deriveDecoder[SectionInfo]
}
