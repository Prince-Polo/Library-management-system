package Process

import Common.API.{API, PlanContext, TraceID}
import Global.{ServerConfig, ServiceCenter}
import Common.DBAPI.{initSchema, writeDB}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import cats.effect.IO
import cats.implicits.given
import io.circe.generic.auto.*
import org.http4s.client.Client

import java.util.UUID

object Init {
  def newList(floor:String,section:String,seat_number_start:Int,seat_number_end:Int):List[(String,String,String,String,String,String,String)]=
    (seat_number_start to seat_number_end).map(_.toString).map((floor,section,_, "available","","false","")).toList
  def init(config: ServerConfig): IO[Unit] = {
    given PlanContext = PlanContext(traceID = TraceID(UUID.randomUUID().toString), 0);
    val F3A=newList("3","A",1,18);
    val F3B=newList("3","B",1,140);
    val F3C=newList("3","C",1,30);
    val F3D=newList("3","D",1,56);
    val F4A=newList("4","A",1,12);
    val F4B=newList("4","B",1,80);
    val F4C=newList("4","C",1,12);
    val F4D=newList("4","D",1,32);
    val F5A=newList("5","A",1,9);
    val F5B=newList("5","B",1,20);
    val seatData=F3A++F3B++F3C++F3D++F4A++F4B++F4C++F4D++F5A++F5B;
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
      _ <- seatData.map { case (floor, section, seatNumber, status, feedback, occupied, studentNumber) =>
        writeDB(
          s"""
             |INSERT INTO $schemaName.seats (floor, section, seat_number, status, feedback, occupied, student_number)
             |VALUES (?, ?, ?, ?, ?, ?, ?)
             |ON CONFLICT (floor, section, seat_number) DO NOTHING
             |""".stripMargin,
          List(SqlParameter("String",floor),
            SqlParameter("String",section),
            SqlParameter("String",seatNumber),
            SqlParameter("String",status),
            SqlParameter("String",feedback),
            SqlParameter("String",occupied),
            SqlParameter("String",studentNumber))
        )
      }.sequence
    } yield ()
  }
}