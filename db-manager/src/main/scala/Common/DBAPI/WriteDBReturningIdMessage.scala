package Common.DBAPI

import Common.API.{PlanContext, Message}
import Common.Object.SqlParameter
import io.circe.{Encoder, Json}
import cats.effect.IO
import java.sql.{Connection, PreparedStatement, SQLException}

case class WriteDBReturningIdMessage(sql: String, parameters: List[SqlParameter]) extends Message[Int] {
  def send(implicit connection: Connection): IO[Int] = IO.delay {
    val preparedStatement: PreparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
    try {
      parameters.zipWithIndex.foreach { case (param, index) =>
        param.dataType.toLowerCase match {
          case "string"  => preparedStatement.setString(index + 1, param.value)
          case "int"     => preparedStatement.setInt(index + 1, param.value.toInt)
          case "double"  => preparedStatement.setDouble(index + 1, param.value.toDouble)
          case "boolean" => preparedStatement.setBoolean(index + 1, param.value.toBoolean)
          case "datetime" => preparedStatement.setTimestamp(index + 1, new java.sql.Timestamp(param.value.toLong))
          case _ => throw new IllegalArgumentException(s"Unsupported data type: ${param.dataType}")
        }
      }
      preparedStatement.executeUpdate()
      val generatedKeys = preparedStatement.getGeneratedKeys
      if (generatedKeys.next()) {
        generatedKeys.getInt(1)
      } else {
        throw new SQLException("Creating entry failed, no ID obtained.")
      }
    } finally {
      preparedStatement.close()
    }
  }
}

object WriteDBReturningIdMessage {
  implicit val writeDBReturningIdMessageEncoder: Encoder[WriteDBReturningIdMessage] = new Encoder[WriteDBReturningIdMessage] {
    final def apply(a: WriteDBReturningIdMessage): Json = Json.obj(
      ("sql", Json.fromString(a.sql)),
      ("parameters", Json.arr(a.parameters.map(p => Json.obj(
        ("dataType", Json.fromString(p.dataType)),
        ("value", Json.fromString(p.value))
      )): _*))
    )
  }
}
