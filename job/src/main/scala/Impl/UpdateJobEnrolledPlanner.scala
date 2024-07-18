package Impl

import cats.effect.IO
import io.circe.syntax._
import io.circe.generic.auto._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class UpdateJobEnrolledPlanner(jobId: Int, increment: Int, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    writeDB(
      s"UPDATE $schemaName.jobs SET jobEnrolled = jobEnrolled + ? WHERE jobId = ?",
      List(
        SqlParameter("Int", increment.toString),
        SqlParameter("Int", jobId.toString)
      )
    ).map { rowsAffected =>
      if (rowsAffected.toInt > 0) {
        "Job enrolled count updated successfully".asJson.noSpaces
      } else {
        "Failed to update job enrolled count".asJson.noSpaces
      }
    }
  }
}
