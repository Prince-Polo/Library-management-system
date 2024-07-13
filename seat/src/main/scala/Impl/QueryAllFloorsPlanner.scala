// Impl.QueryAllFloorsPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.SeatAPI.QueryAllFloorsResponse
import Common.FloorInfo

case class QueryAllFloorsPlanner(override val planContext: PlanContext) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    readDBRows(
      s"""
         |SELECT floor, COUNT(DISTINCT section) as sections, COUNT(seat_number) as total_seats,
         |SUM(CASE WHEN occupied = 'false' THEN 1 ELSE 0 END) as free_seats
         |FROM $schemaName.seats
         |GROUP BY floor
         |ORDER BY floor
       """.stripMargin,
      List()
    ).map { rows =>
      val floors = rows.map { row =>
        FloorInfo(
          floor = row.hcursor.get[String]("floor").getOrElse(""),
          sections = row.hcursor.get[String]("sections").getOrElse(""),
          totalSeats = row.hcursor.get[String]("total_seats").getOrElse(""),
          freeSeats = row.hcursor.get[String]("free_seats").getOrElse("")
        )
      }.toList
      QueryAllFloorsResponse(floorCount = floors.size.toString, floors).asJson.noSpaces
    }
  }
}
