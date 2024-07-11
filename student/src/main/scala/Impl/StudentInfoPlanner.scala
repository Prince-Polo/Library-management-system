package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.{StudentInfoResponse}
import Common.{BasicInfo, Info}

case class StudentInfoPlanner(number: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT user_name, number, volunteer_status, floor, section_number, seat_number, violation_count, volunteer_hours
         |FROM $schemaName.students
         |WHERE number = ?
         |""".stripMargin,
      List(SqlParameter("String", number))
    ).map {
      case Nil =>
        s"""{"error": "Student not found"}"""
      case rows =>
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

            StudentInfoResponse(
              userName,
              number,
              volunteerStatus,
              floor,
              sectionNumber,
              seatNumber,
              violationCount,
              volunteerHours
            ).asJson.noSpaces
          case None =>
            s"""{"error": "Student not found"}"""
        }
    }
  }
}
