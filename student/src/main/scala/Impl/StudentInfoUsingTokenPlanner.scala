package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentInfoUsingTokenResponse
import Utils.JWTUtil

object CustomDecoders {
  implicit val floatDecoder: Decoder[Float] = Decoder.decodeString.emap { str =>
    try Right(str.toFloat)
    catch { case _: NumberFormatException => Left("Invalid float format") }
  }

  implicit val intDecoder: Decoder[Int] = Decoder.decodeString.emap { str =>
    try Right(str.toInt)
    catch { case _: NumberFormatException => Left("Invalid integer format") }
  }
}

import CustomDecoders._

case class StudentInfoUsingTokenPlanner(token: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap { number =>
        readDBRows(
          s"""
             |SELECT user_name, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours
             |FROM $schemaName.students
             |WHERE number = ?
            """.stripMargin,
          List(SqlParameter("String", number))
        ).flatMap { rows =>
          IO.raiseWhen(rows.isEmpty)(new Exception("Student not found")).map { _ =>
            val cursor = rows.head.hcursor
            StudentInfoUsingTokenResponse(
              userName = cursor.get[String]("user_name").getOrElse(""),
              volunteerStatus = cursor.get[Boolean]("volunteer_status").getOrElse(false),
              floor = cursor.get[String]("floor").getOrElse("0"),
              sectionNumber = cursor.get[String]("section_number").getOrElse("0"),
              seatNumber = cursor.get[String]("seat_number").getOrElse("0"),
              violationCount = cursor.get[Int]("violation_count").getOrElse(0),
              volunteerHours = cursor.get[Float]("volunteer_hours").getOrElse(0.0f)
            ).asJson.noSpaces
          }
        }
      }.handleError { error =>
        s"""{"error": "${error.getMessage}"}"""
      }
}
