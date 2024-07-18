package Impl

import Common.API.{PlanContext, TraceID}
import Common.Object.{ParameterList, SqlParameter}
import cats.effect.{IO, Ref}
import java.sql.{Connection, PreparedStatement, Timestamp}

case class WriteDBListMessagePlanner(sqlStatement: String, parameters: List[ParameterList], override val planContext: PlanContext) extends DBPlanner[String] {
  override def planWithConnection(connection: Connection, connectionMap: Ref[IO, Map[String, Connection]]): IO[String] = IO.delay {
    val preparedStatement = connection.prepareStatement(sqlStatement)
    try {
      if (parameters.isEmpty)
        preparedStatement.executeUpdate()
      else
        parameters.foreach { parameterList =>
          // Reset the statement for each set of parameters
          preparedStatement.clearParameters()

          // Set parameters for the current execution
          parameterList.l.zipWithIndex.foreach { case (sqlParameter, index) =>
            setPreparedStatement(preparedStatement, index + 1, sqlParameter, connection)
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
  private def setPreparedStatement(statement: PreparedStatement, index: Int, sqlParameter: SqlParameter, connection: Connection): Unit = {
    // This is a simplified version; you might need to extend this method
    // to handle different types more accurately.
    sqlParameter.dataType.toLowerCase match {
      case "string" => statement.setString(index, sqlParameter.value)
      case "int"    => statement.setInt(index, sqlParameter.value.toInt)
      case "double" => statement.setDouble(index, sqlParameter.value.toDouble)
      case "datetime" => statement.setTimestamp(index, new Timestamp(sqlParameter.value.toLong)) // Convert DateTime to Timestamp
      case "stringarray" =>
        val array: Array[Object] = sqlParameter.value.stripPrefix("{").stripSuffix("}").split(",").map(_.asInstanceOf[Object])
        statement.setArray(index, connection.createArrayOf("TEXT", array))
      case "string2darray" =>
        val arrayOfArrays: Array[Object] = sqlParameter.value.stripPrefix("{").stripSuffix("}").split(";").map(subArray => connection.createArrayOf("TEXT", subArray.split(",").map(_.asInstanceOf[Object])))
        statement.setArray(index, connection.createArrayOf("TEXT", arrayOfArrays))
      // Add more cases for other data types
      case _ => throw new IllegalArgumentException(s"Unhandled parameter type: ${sqlParameter.dataType}")
    }
  }
}
