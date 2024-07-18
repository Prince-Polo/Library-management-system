package Impl

import Common.API.{PlanContext, TraceID}
import Common.Object.SqlParameter
import cats.effect.{IO, Ref}
import java.sql.{Connection, PreparedStatement, Timestamp}

case class WriteDBMessagePlanner(sqlStatement: String, parameters: List[SqlParameter], override val planContext: PlanContext) extends DBPlanner[String] {
  override def planWithConnection(connection: Connection, connectionMap: Ref[IO, Map[String, Connection]]): IO[String] = IO.delay {
    val preparedStatement = connection.prepareStatement(sqlStatement)
    try {
      if (parameters.isEmpty)
        preparedStatement.executeUpdate()
      else {
        // Reset the statement for each set of parameters
        preparedStatement.clearParameters()

        println(parameters)
        // Set parameters for the current execution
        parameters.zipWithIndex.foreach { case (sqlParameter, index) =>
          setPreparedStatement(preparedStatement, index + 1, sqlParameter)
        }

        // Execute the update for the current set of parameters
        preparedStatement.executeUpdate()
      }
      "Operation(s) done successfully"
    } finally {
      preparedStatement.close() // Ensure the PreparedStatement is closed after use
    }
  }

  // Function to set the PreparedStatement parameter based on SqlParameter
  private def setPreparedStatement(statement: PreparedStatement, index: Int, sqlParameter: SqlParameter): Unit = {
    println("set " + sqlParameter + index + statement)
    // This is a simplified version; you might need to extend this method
    // to handle different types more accurately.
    sqlParameter.dataType.toLowerCase match {
      case "string" => statement.setString(index, sqlParameter.value)
      case "int" => statement.setInt(index, sqlParameter.value.toInt)
      case "boolean" => statement.setBoolean(index, sqlParameter.value.toBoolean)
      case "datetime" => statement.setTimestamp(index, Timestamp.valueOf(sqlParameter.value))
      case "stringarray" =>
        val array = statement.getConnection.createArrayOf("TEXT", sqlParameter.value.stripPrefix("{").stripSuffix("}").split(",").map(_.trim))
        statement.setArray(index, array)
      case "string2darray" =>
        val arrayOfArrays = sqlParameter.value.stripPrefix("{").stripSuffix("}").split(";").map(_.stripPrefix("{").stripSuffix("}").split(",").map(_.trim).asInstanceOf[Array[AnyRef]]).asInstanceOf[Array[AnyRef]]
        val array = statement.getConnection.createArrayOf("TEXT", arrayOfArrays)
        statement.setArray(index, array)
      case _ => throw new Exception(s"Unsupported data type: ${sqlParameter.dataType}")
    }
  }
}