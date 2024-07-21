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
      // 创建或更新 students 表
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS $schemaName.students (
           |  user_name TEXT,
           |  password TEXT,
           |  number TEXT,
           |  volunteer_status BOOLEAN,                 -- 修改为 BOOLEAN 类型
           |  floor TEXT,
           |  section_number TEXT,
           |  seat_number TEXT,
           |  violation_count INT,                      -- 使用 INT 类型
           |  volunteer_hours FLOAT,                    -- 使用 FLOAT 类型
           |  PRIMARY KEY (number)
           |)
           |""".stripMargin, List()
      )
      // 创建 student_tokens 表
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS $schemaName.student_tokens (
           |  number TEXT PRIMARY KEY,
           |  token TEXT
           |)
           |""".stripMargin, List()
      )
    } yield ()
  }
}
