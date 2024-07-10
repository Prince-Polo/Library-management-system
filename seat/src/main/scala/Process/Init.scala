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
      // 创建座位状态的枚举类型
      _ <- writeDB(
        s"""
           |DO $$
           |BEGIN
           |   IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typename = 'seat_status') THEN
           |      CREATE TYPE seat_status AS ENUM ('Normal', 'Reported', 'Confirmed');
           |   END IF;
           |END $$;
           |""".stripMargin, List()
      )
      // 创建或更新 seats 表，包含所有必要的字段
      _ <- writeDB(
        s"""
           |CREATE TABLE IF NOT EXISTS ${schemaName}.seats (
           |  floor INT,
           |  section INT,
           |  seat_number INT,
           |  status seat_status,
           |  feedback TEXT,
           |  occupied BOOLEAN,
           |  student_number TEXT,
           |  PRIMARY KEY (floor, section, seat_number)
           |)
           |""".stripMargin, List()
      )
    } yield ()
  }
}
