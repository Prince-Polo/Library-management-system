package Process

import Common.API.{API, PlanContext, TraceID}
import Global.{ServerConfig, ServiceCenter}
import Common.DBAPI.{initSchema, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*
import org.http4s.client.Client

import java.util.UUID

object Init {
  def init(config:ServerConfig):IO[Unit]=
    given PlanContext=PlanContext(traceID = TraceID(UUID.randomUUID().toString),0)

    val adminName = SqlParameter("String", "Admin")
    val adminPassword = SqlParameter("String", "Admin")
    val adminID = SqlParameter("String", "0")

    for{
      _ <- API.init(config.maximumClientConnection)
      _ <- initSchema(schemaName)
      _ <- writeDB(s"CREATE TABLE IF NOT EXISTS ${schemaName}.admin (AdminName TEXT, AdminPassword TEXT, AdminID TEXT)", List())
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS ${schemaName}.job (
           |  jobId BIGINT,
           |  jobStudentId TEXT,
           |  jobShortDescription TEXT,
           |  jobLongDescription TEXT,
           |  jobHardness BIGINT,
           |  jobCredit BIGINT,
           |  jobComplete BOOLEAN,
           |  jobBooked BOOLEAN,
           |  jobApproved BOOLEAN
           |)
         """.stripMargin,
        List()
      )
      _ <- writeDB(s"INSERT INTO ${schemaName}.admin (AdminName, AdminPassword, AdminID) VALUES (?, ?, ?)", List(adminName, adminPassword, adminID))
    } yield ()

}
