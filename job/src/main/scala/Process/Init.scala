package Process

import Common.API.{API, PlanContext, TraceID}
import Global.{ServerConfig, ServiceCenter}
import Common.DBAPI.{initSchema, writeDB}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.client.Client

import java.util.UUID

object Init {
  def init(config: ServerConfig): IO[Unit] =
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), 0)
    for {
      _ <- API.init(config.maximumClientConnection)
      _ <- initSchema(schemaName)
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS ${schemaName}.jobs (
           |  jobId SERIAL PRIMARY KEY,
           |  jobShortDescription TEXT,
           |  jobLongDescription TEXT,
           |  jobHardness INT,
           |  jobCredit INT,
           |  jobComplete BOOLEAN DEFAULT FALSE,
           |  jobBooked BOOLEAN DEFAULT FALSE,
           |  jobApproved BOOLEAN DEFAULT FALSE,
           |  jobCurrent INT, -- 现有预约人数
           |  jobRequired INT, -- 任务需要的人数
           |  jobEnrolled INT -- 现有通过人数
           |)
         """.stripMargin,
        List()
      )
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS ${schemaName}.tasks (
           |  taskId SERIAL PRIMARY KEY,
           |  studentId TEXT,
           |  status INT CHECK (status IN (0, 1, 2))
           |)
         """.stripMargin,
        List()
      )
    } yield ()
}

