package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.StudentAPI.UpdateViolationCountResponse

case class UpdateViolationCountPlanner(number: String, violationCount: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] =
    writeDB(s"UPDATE $schemaName.students SET violation_count = ? WHERE number = ?", List(SqlParameter("String", violationCount.toString), SqlParameter("String", number)))
      .map(rowsAffected => UpdateViolationCountResponse(success = rowsAffected.nonEmpty, message = if (rowsAffected.nonEmpty) s"Updated violation count for student with number: $number" else s"Failed to update violation count for student with number: $number").asJson.noSpaces)
      .handleError(error => UpdateViolationCountResponse(success = false, message = error.getMessage).asJson.noSpaces)
}