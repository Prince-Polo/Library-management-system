package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBRows, readDBBoolean, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentLoginResponse
import Utils.JWTUtil

case class StudentLoginMessagePlanner(userName: String, password: String, number: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? OR number = ?)", List(SqlParameter("String", userName), SqlParameter("String", number)))
      .flatMap(exists => IO.raiseWhen(!exists)(new Exception("Invalid user")))
      .flatMap(_ => readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.students WHERE user_name = ? AND password = ?)", List(SqlParameter("String", userName), SqlParameter("String", password))))
      .flatMap(passwordValid => IO.raiseWhen(!passwordValid)(new Exception("Wrong password")))
      .flatMap(_ => readDBRows(s"SELECT user_name, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours FROM ${schemaName}.students WHERE user_name = ? AND password = ? AND number = ?", List(SqlParameter("String", userName), SqlParameter("String", password), SqlParameter("String", number)))
        .flatMap(rows =>
          IO.raiseWhen(rows.isEmpty)(new Exception("User not found"))
            .map(_ => rows.head.hcursor)
            .flatMap(cursor =>
              writeDB(s"INSERT INTO ${schemaName}.student_tokens (number, token) VALUES (?, ?) ON CONFLICT(number) DO UPDATE SET token = EXCLUDED.token", List(SqlParameter("String", number), SqlParameter("String", JWTUtil.createToken(number))))
                .as(StudentLoginResponse(
                  valid = true,
                  userName = cursor.get[String]("userName").getOrElse(""),
                  token = JWTUtil.createToken(number),
                  volunteerStatus = cursor.get[Boolean]("volunteerStatus").getOrElse(false),
                  floor = cursor.get[String]("floor").getOrElse("0"),
                  sectionNumber = cursor.get[String]("sectionNumber").getOrElse("0"),
                  seatNumber = cursor.get[String]("seatNumber").getOrElse("0"),
                  violationCount = cursor.get[Int]("violationCount").getOrElse(0),
                  volunteerHours = cursor.get[Float]("volunteerHours").getOrElse(0.0f)
                ).asJson.noSpaces)
            )
        )
      ).handleError(_ => StudentLoginResponse(valid = false, userName = "", token = "", volunteerStatus = false, floor = "0", sectionNumber = "0", seatNumber = "0", violationCount = 0, volunteerHours = 0.0f).asJson.noSpaces)
}
