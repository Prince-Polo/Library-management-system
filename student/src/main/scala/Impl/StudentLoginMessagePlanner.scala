package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import _root_.APIs.StudentAPI.{StudentLoginMessage, StudentLoginResponse}
import Common.BasicInfo

case class StudentLoginMessagePlanner(message: StudentLoginMessage, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // Attempt to validate the user by reading the rows from the database
    val messageInfo = message.info
    val checkUserExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? OR number = ?)",
      List(
        SqlParameter("String", messageInfo.userName),
        SqlParameter("String", messageInfo.number)
      )
    )

    checkUserExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Invalid user"))
      } else {
        val checkPassword = readDBBoolean(
          s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? AND password = ?)",
          List(
            SqlParameter("String", messageInfo.userName),
            SqlParameter("String", messageInfo.password)
          )
        )

        checkPassword.flatMap { exists =>
          if (!exists) {
            IO.raiseError(new Exception("Wrong password"))
          } else {
            // Retrieve additional information like id and authority if needed
            readDBRows(
              s"SELECT id, authority FROM ${schemaName}.students WHERE user_name = ? AND password = ? AND number = ?",
              List(
                SqlParameter("String", messageInfo.userName),
                SqlParameter("String", messageInfo.password),
                SqlParameter("String", messageInfo.number)
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
