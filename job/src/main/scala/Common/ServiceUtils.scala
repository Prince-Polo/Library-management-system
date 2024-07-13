package Common

import Global.GlobalVariables.serviceCode
import Global.ServiceCenter.fullNameMap
import cats.effect.IO
import com.comcast.ip4s.Port
import org.http4s.Uri

object ServiceUtils {
  def getURI(serviceCode: String): IO[Uri] =
    IO.fromEither(Uri.fromString(
      "http://localhost:" + getPort(serviceCode).value.toString + "/"
    ))

  def getPort(serviceCode: String): Port =
    Port.fromInt(portMap(serviceCode)).getOrElse(
      throw new IllegalArgumentException(s"Invalid port for serviceCode: $serviceCode")
    )

  def serviceName(serviceCode: String): String = {
    val fullName = fullNameMap.getOrElse(serviceCode, throw new IllegalArgumentException(s"Service code not found: $serviceCode"))
    val start = fullName.indexOf("（")
    val end = fullName.indexOf("）")

    if (start >= 0 && end > start) {
      fullName.substring(start + 1, end).toLowerCase
    } else {
      throw new IllegalArgumentException(s"Invalid full name format for serviceCode: $serviceCode")
    }
  }

  def portMap(serviceCode: String): Int = {
    serviceCode.drop(1).toInt +
      (if (serviceCode.head == 'A') 10000 else if (serviceCode.head == 'D') 20000 else 30000)
  }

  lazy val servicePort: Int = portMap(serviceCode)
  lazy val serviceFullName: String = fullNameMap.getOrElse(serviceCode, throw new IllegalArgumentException(s"Service code not found: $serviceCode"))
  lazy val serviceShortName: String = serviceName(serviceCode)
  lazy val schemaName: String = serviceName(serviceCode)
}
