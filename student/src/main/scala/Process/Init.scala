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
  def init(config: ServerConfig): IO[Unit] = {
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), 0)
    for {
      _ <- API.init(config.maximumClientConnection)
      _ <- initSchema(schemaName)
      // 创建或更新 students 表，包含所有必要的字段
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS $schemaName.students (
           |  user_name TEXT,
           |  password TEXT,
           |  number TEXT,
           |  volunteer_status BOOLEAN,
           |  floor INT,
           |  section_number INT,
           |  seat_number INT,
           |  violation_count INT,
           |  volunteer_hours INT,
           |  PRIMARY KEY (number)
           |)
           |""".stripMargin, List()
      )
    } yield ()
  }
}
