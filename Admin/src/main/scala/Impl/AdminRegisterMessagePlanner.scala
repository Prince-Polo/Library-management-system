package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class AdminRegisterMessagePlanner(AdminName: String, AdminPassword: String, AdminID: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using planContext: PlanContext): IO[String] = {
    // Check if the administrator is already registered
    val checkExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.admin WHERE AdminName = ?)",
      List(SqlParameter("String", AdminName))
    )

    checkExists.flatMap { exists =>
      if (exists) {
        IO.raiseError(new Exception("Administrator already registered"))
      } else {
        // Insert new administrator into the database
        writeDB(
          s"INSERT INTO ${schemaName}.admin (AdminName, AdminPassword, AdminEmail, AdminID) VALUES (?, ?, ?, ?)",
          List(SqlParameter("String", AdminName), SqlParameter("String", AdminPassword), SqlParameter("String", AdminID))
        ).map(_ => "Registration successful")
      }
    }
  }