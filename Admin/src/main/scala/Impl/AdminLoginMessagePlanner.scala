package Impl

import cats.effect.IO
import io.circe.Json
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*



import io.circe.syntax._
import io.circe.Json
import Common.API.PlanContext
import Common.Object.SqlParameter
import cats.effect.IO

case class AdminLoginMessagePlanner(AdminName: String, AdminPassword: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // Attempt to validate the admin by reading the rows from the database
    val checkAdminExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.admin WHERE AdminName = ?)",
      List(SqlParameter("String", AdminName))
    )
    checkAdminExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Invalid admin user"))
      } else {
        val checkPassword = readDBBoolean(
          s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.admin WHERE AdminName = ? AND AdminPassword = ?)",
          List(SqlParameter("String", AdminName), SqlParameter("String", AdminPassword))
        )
        checkPassword.flatMap { passwordExists =>
          if (!passwordExists) {
            IO.raiseError(new Exception("Wrong password"))
          } else {
            val getAdminDetails =readDBRows(
              s"SELECT * FROM ${schemaName}.admin WHERE AdminName = ?",
              List(SqlParameter("String", AdminName))
            )
            getAdminDetails.map { adminList =>
              adminList.map { json =>
                json.noSpaces // Convert each Json object to a compact string representation
              }.mkString("\n") // Combine all JSON strings with newline separator
            }
          }
        }
      }
    }
  }
}
