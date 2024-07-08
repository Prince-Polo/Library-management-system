package Common.Object

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, HCursor}

// 定义 SqlParameter 案例类
case class SqlParameter(dataType: String, value: String)

object SqlParameter {
  // 为 SqlParameter 生成编码器
  implicit val encodeSqlParameter: Encoder[SqlParameter] = deriveEncoder[SqlParameter]

  // 自定义解码器
  implicit val decodeSqlParameter: Decoder[SqlParameter] = new Decoder[SqlParameter] {
    final def apply(c: HCursor): Decoder.Result[SqlParameter] = for {
      dataType <- c.downField("dataType").as[String]
      value <- c.downField("value").as[String]
    } yield {
      dataType.toLowerCase match {
        case "string" => SqlParameter("String", value)
        case "int" => SqlParameter("Int", value)
        case "boolean" => SqlParameter("Boolean", value)
        case "datetime" => SqlParameter("DateTime", value)
        case "float" => SqlParameter("Float", value)
        case "double" => SqlParameter("Double", value)
        // 可以根据需要添加更多类型
        case _ => throw new Exception("Unsupported data type")
      }
    }
  }
}
