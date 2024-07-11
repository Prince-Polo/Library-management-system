package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Common.BasicInfo
import APIs.StudentAPI.{StudentLoginResponse}

case class StudentLoginMessagePlanner(info: BasicInfo, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // Attempt to validate the user by reading the rows from the database
    val checkUserExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? OR number = ?)",
      List(
        SqlParameter("String", info.userName),
        SqlParameter("String", info.number)
      )
    )

    checkUserExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Invalid user"))
      } else {
        val checkPassword = readDBBoolean(
          s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? AND password = ?)",
          List(
            SqlParameter("String", info.userName),
            SqlParameter("String", info.password)
          )
        )

        checkPassword.flatMap { passwordValid =>
          if (!passwordValid) {
            IO.raiseError(new Exception("Wrong password"))
          } else {
            // Retrieve additional information like id and authority if needed
            readDBRows(
              s"SELECT id, authority FROM ${schemaName}.students WHERE user_name = ? AND password = ? AND number = ?",
              List(
                SqlParameter("String", info.userName),
                SqlParameter("String", info.password),
                SqlParameter("String", info.number)
              )
            ).map { rows =>
              rows.headOption match {
                case Some(row) =>
                  val id = row.hcursor.get[Int]("id").getOrElse(-1)
                  val authority = row.hcursor.get[Int]("authority").getOrElse(-1)
                  StudentLoginResponse(valid = true, Some(id), Some(authority)).asJson.noSpaces
                case None =>
                  StudentLoginResponse(valid = true).asJson.noSpaces
              }
            }
          }
        }
      }
    }
  }
}
