// File: Impl/UpdateViolationCountPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.UpdateViolationCountResponse

case class UpdateViolationCountPlanner(number: String, violationCount: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.students SET violation_count = ? WHERE number = ?",
      List(
        SqlParameter("String", violationCount),
        SqlParameter("String", number)
      )
    ).map { rowsAffected =>
      if (rowsAffected!="") {
        UpdateViolationCountResponse(success = true, message = s"Updated violation count for student with number: $number").asJson.noSpaces
      } else {
        UpdateViolationCountResponse(success = false, message = s"Failed to update violation count for student with number: $number").asJson.noSpaces
      }
    }
  }
}
