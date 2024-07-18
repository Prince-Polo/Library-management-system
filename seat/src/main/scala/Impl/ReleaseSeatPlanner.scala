// Impl.ReleaseSeatPlanner.scala
package Impl

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBRows
import Common.DBAPI.writeDB
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class ReleaseSeatPlanner(floor: String, section: String, seatNumber: String) extends Planner[String] {
  override def plan(using planContext: PlanContext): IO[String] = {
    for {
      // Update the seat to be unoccupied
      result <- writeDB(
        s"""
           |UPDATE $schemaName.seats
           |SET occupied = 'false'
           |WHERE floor = ? AND section = ? AND seat_number = ?
         """.stripMargin,
        List(
          SqlParameter("String", floor),
          SqlParameter("String", section),
          SqlParameter("String", seatNumber)
        )
      )
    } yield {
      if (result.nonEmpty) {
        s"Seat $seatNumber in section $section on floor $floor is now unoccupied.".asJson.noSpaces
      } else {
        s"Failed to release seat $seatNumber in section $section on floor $floor.".asJson.noSpaces
      }
    }
  }
}

