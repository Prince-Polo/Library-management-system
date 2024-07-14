package Utils

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import java.time.Instant
import scala.util.{Success, Failure}
import com.typesafe.config.ConfigFactory

object JWTUtil {
  private val config = ConfigFactory.load()
  private val secretKey = config.getString("jwt.secret")
  private val expirationTime = config.getLong("jwt.expiration-time")

  def createToken(username: String): String = {
    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(expirationTime).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond),
      subject = Some(username)
    )
    Jwt.encode(claim, secretKey, JwtAlgorithm.HS256)
  }

  def validateToken(token: String): Boolean = {
    Jwt.decode(token, secretKey, Seq(JwtAlgorithm.HS256)) match {
      case Success(_) => true
      case Failure(_) => false
    }
  }
}
