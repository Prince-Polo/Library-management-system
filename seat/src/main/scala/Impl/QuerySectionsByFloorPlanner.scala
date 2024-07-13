// Impl.QuerySectionsByFloorPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.{QuerySectionsByFloorResponse}
import Common.SectionInfo

case class QuerySectionsByFloorPlanner(floor: String, override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT section, COUNT(seat_number) as total_seats,
         |SUM(CASE WHEN occupied = 'false' THEN 1 ELSE 0 END) as free_seats
         |FROM $schemaName.seats
         |WHERE floor = ?
         |GROUP BY section
         |ORDER BY section
       """.stripMargin,
      List(SqlParameter("String", floor))
    ).map { rows =>
      val sections = rows.map { row =>
        SectionInfo(
          section = row.hcursor.get[String]("section").getOrElse(""),
          totalSeats = row.hcursor.get[String]("total_seats").getOrElse(""),
          freeSeats = row.hcursor.get[String]("free_seats").getOrElse("")
        )
      }.toList
      QuerySectionsByFloorResponse(sectionCount = sections.size.toString, sections).asJson.noSpaces
    }
  }
}
