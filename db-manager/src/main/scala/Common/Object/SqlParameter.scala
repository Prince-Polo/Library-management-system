package Common.Object

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, HCursor}

// Define the SqlParam case class
case class SqlParameter(dataType: String, value: String)

object SqlParameter {
  // Encoder for SqlParam
  implicit val encodeSqlParameter: Encoder[SqlParameter] = deriveEncoder[SqlParameter]

  // Decoder for SqlParam
  implicit val decodeSqlParameter: Decoder[SqlParameter] = new Decoder[SqlParameter] {
    final def apply(c: HCursor): Decoder.Result[SqlParameter] = for {
      dataType <- c.downField("dataType").as[String]
      value <- c.downField("value").as[String]
    } yield {
      dataType.toLowerCase match {
        case "string" => SqlParameter("String", value)
        case "int" => SqlParameter("Int", value)
        case "boolean" => SqlParameter("Boolean", value)
        case "float" => SqlParameter("Float", value)
        case "datetime" => SqlParameter("DateTime", value)
        case "stringarray" => SqlParameter("StringArray", value)
        case "string2darray" => SqlParameter("String2DArray", value) // Add 2D Array type case
        case _ => throw new Exception("Unsupported data type")
      }
    }
  }
}
