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
      // 创建或更新 seats 表，包含所有必要的字段
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS $schemaName.seats (
           |  floor TEXT,  -- 存储字符串类型
           |  section TEXT,  -- 存储字符串类型
           |  seat_number TEXT,  -- 存储字符串类型
           |  status TEXT,  -- 存储字符串类型
           |  feedback TEXT,
           |  occupied TEXT,  -- 存储字符串类型
           |  student_number TEXT,
           |  PRIMARY KEY (floor, section, seat_number)
           |)
           |""".stripMargin, List()
      )
    } yield ()
  }
}
