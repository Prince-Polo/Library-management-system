package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{StudentLoginResponse}

case class StudentLoginMessagePlanner(
                                       userName: String,
                                       password: String,
                                       number: String,
                                       override val planContext: PlanContext
                                     ) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    // Attempt to validate the user by reading the rows from the database
    val checkUserExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? OR number = ?)",
      List(
        SqlParameter("String", userName),
        SqlParameter("String", number)
      )
    )

    checkUserExists.flatMap { exists =>
      if (!exists) {
        IO.raiseError(new Exception("Invalid user"))
      } else {
        val checkPassword = readDBBoolean(
          s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? AND password = ?)",
          List(
            SqlParameter("String", userName),
            SqlParameter("String", password)
          )
        )

        checkPassword.flatMap { passwordValid =>
          if (!passwordValid) {
            IO.raiseError(new Exception("Wrong password"))
          } else {
            // Retrieve additional information
            readDBRows(
              s"SELECT user_name, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours FROM ${schemaName}.students WHERE user_name = ? AND password = ? AND number = ?",
              List(
                SqlParameter("String", userName),
                SqlParameter("String", password),
                SqlParameter("String", number)
              )
            ).map { rows =>
              rows.headOption match {
                case Some(row) =>
                  val userName = row.hcursor.get[String]("user_name").getOrElse("")
                  val number = row.hcursor.get[String]("number").getOrElse("")
                  val volunteerStatus = row.hcursor.get[Boolean]("volunteer_status").getOrElse(false)
                  val floor = row.hcursor.get[Int]("floor").getOrElse(0)
                  val sectionNumber = row.hcursor.get[Int]("section_number").getOrElse(0)
                  val seatNumber = row.hcursor.get[Int]("seat_number").getOrElse(0)
                  val violationCount = row.hcursor.get[Int]("violation_count").getOrElse(0)
                  val volunteerHours = row.hcursor.get[Int]("volunteer_hours").getOrElse(0)
                  StudentLoginResponse(
                    valid = true,
                    Some(userName),
                    Some(number),
                    Some(volunteerStatus),
                    Some(floor),
                    Some(sectionNumber),
                    Some(seatNumber),
                    Some(violationCount),
                    Some(volunteerHours)
                  ).asJson.noSpaces
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
