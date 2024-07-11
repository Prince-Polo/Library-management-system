package Global

import Global.GlobalVariables.serviceCode
import cats.effect.IO
import com.comcast.ip4s.Port
import org.http4s.Uri

object ServiceCenter {
  val projectName: String = "APP"

  val dbManagerServiceCode = "A000001"
  val adminServiceCode     = "A000002"
  val studentServiceCode   = "A000003"
  val portalServiceCode    = "A000004"
  val seatServiceCode      = "A000005"
  val jobServiceCode       = "A000006"

  val fullNameMap: Map[String, String] = Map(
    dbManagerServiceCode ->  "数据库管理（DB_Manager）",
    adminServiceCode    ->  "管理员（Administrator）",
    studentServiceCode   ->  "学生（Student）",
    portalServiceCode    ->  "门户（Portal）",
    seatServiceCode      ->  "座位（Seat）",
    jobServiceCode -> "任务（Job)"
  )

  val address: Map[String, String] = Map(
    "DB-Manager" ->     "127.0.0.1:10001",
    "Admin" ->         "127.0.0.1:10002",
    "Patient" ->        "127.0.0.1:10003",
    "Portal" ->         "127.0.0.1:10004",
    "Seat" ->           "127.0.0.1:10005",
    "Job" ->         "127.0.0.1:10006"
  )
}
