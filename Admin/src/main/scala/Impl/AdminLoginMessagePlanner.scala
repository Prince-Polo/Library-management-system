package Impl

import cats.effect.IO
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI._
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

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
            // Successful login, return an empty response
            IO.pure("")
          }
        }
      }
    }
  }
}
