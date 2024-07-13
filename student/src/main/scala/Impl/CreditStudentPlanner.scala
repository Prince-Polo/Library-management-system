package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.CreditStudentResponse

case class CreditStudentPlanner(number: String, volunteerHours: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.students SET volunteer_hours = ? WHERE number = ?",
      List(
        SqlParameter("String", volunteerHours),
        SqlParameter("String", number)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        CreditStudentResponse(success = true, message = s"Updated volunteer hours for student with number: $number").asJson.noSpaces
      } else {
        CreditStudentResponse(success = false, message = s"Failed to update volunteer hours for student with number: $number").asJson.noSpaces
      }
    }
  }
}
