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
  override def plan(using planContext: PlanContext): IO[String] =
    readDBRows(
      s"""
         |SELECT floor, COUNT(DISTINCT section) as sections, COUNT(seat_number) as total_seats,
         |SUM(CASE WHEN occupied = false AND status != 'Confirmed' THEN 1 ELSE 0 END) as free_seats
         |FROM $schemaName.seats
         |GROUP BY floor
         |ORDER BY floor
       """.stripMargin,
      List()
    ).map(rows =>
      QueryAllFloorsResponse(
        floorCount = rows.size.toString,
        floors = rows.map(row =>
          FloorInfo(
            floor = row.hcursor.get[String]("floor").getOrElse(""),
            sections = row.hcursor.get[Int]("sections").getOrElse(0).toString,
            totalSeats = row.hcursor.get[Int]("totalSeats").getOrElse(0).toString,
            freeSeats = row.hcursor.get[Int]("freeSeats").getOrElse(0).toString
          )
        ).toList
      ).asJson.noSpaces
    ).handleError(error => QueryAllFloorsResponse(floorCount = "0", floors = List()).asJson.noSpaces)
}
