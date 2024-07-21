package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, readDBBoolean}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.StudentRegisterResponse
import Utils.JWTUtil.createToken

case class StudentRegisterPlanner(userName: String, password: String, number: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    readDBBoolean(s"SELECT EXISTS(SELECT 1 FROM $schemaName.students WHERE user_name = ? OR number = ?)", List(SqlParameter("String", userName), SqlParameter("String", number)))
      .flatMap(exists => IO.raiseWhen(exists)(new Exception("Student already registered")))
      .flatMap(_ => writeDB(s"INSERT INTO $schemaName.students (user_name, password, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", List(SqlParameter("String", userName), SqlParameter("String", password), SqlParameter("String", number), SqlParameter("String", false.toString), SqlParameter("String", "0"), SqlParameter("String", "0"), SqlParameter("String", "0"), SqlParameter("String", 0.toString), SqlParameter("String", 0.0f.toString))))
      .flatMap(_ => writeDB(s"INSERT INTO $schemaName.student_tokens (number, token) VALUES (?, ?)", List(SqlParameter("String", number), SqlParameter("String", createToken(number)))))
      .map(_ => StudentRegisterResponse(userName = userName, volunteerStatus = false, floor = "0", sectionNumber = "0", seatNumber = "0", violationCount = 0, volunteerHours = 0.0f, token = createToken(number)).asJson.noSpaces)
      .handleError(error => StudentRegisterResponse(userName = "", volunteerStatus = false, floor = "", sectionNumber = "", seatNumber = "", violationCount = 0, volunteerHours = 0.0f, token = "").asJson.noSpaces)
}
