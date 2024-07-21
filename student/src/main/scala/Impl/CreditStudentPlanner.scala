package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.CreditStudentResponse
import Utils.JWTUtil

case class CreditStudentPlanner(token: String, volunteerHours: Float, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    IO.fromOption(JWTUtil.getNumber(token))(new Exception("Invalid token"))
      .flatMap(number =>
        writeDB(
          s"UPDATE $schemaName.students SET volunteer_hours = ? WHERE number = ?",
          List(
            SqlParameter("Float", volunteerHours.toString),  // 更改为 Float 类型
            SqlParameter("String", number)
          )
        ).as(CreditStudentResponse(success = true, message = s"Updated volunteer hours for student with number: $number").asJson.noSpaces)
      ).handleError(error => CreditStudentResponse(success = false, message = error.getMessage).asJson.noSpaces)
}
