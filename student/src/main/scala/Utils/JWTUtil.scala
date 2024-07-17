package Utils

import io.circe.syntax._
import io.circe.parser.decode
import io.circe.{Encoder, Decoder}
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object JWTUtil {
  private val secretKey = "your-secret-key"
  private val algorithm = "HmacSHA256"

  case class JwtHeader(alg: String = "HS256", typ: String = "JWT")
  case class JwtPayload(number: String, exp: Long)

  implicit val jwtHeaderEncoder: Encoder[JwtHeader] = Encoder.forProduct2("alg", "typ")(u => (u.alg, u.typ))
  implicit val jwtPayloadEncoder: Encoder[JwtPayload] = Encoder.forProduct2("number", "exp")(u => (u.number, u.exp))

  implicit val jwtHeaderDecoder: Decoder[JwtHeader] = Decoder.forProduct2("alg", "typ")(JwtHeader.apply)
  implicit val jwtPayloadDecoder: Decoder[JwtPayload] = Decoder.forProduct2("number", "exp")(JwtPayload.apply)

  private def encodeBase64(input: String): String = Base64.getUrlEncoder.withoutPadding.encodeToString(input.getBytes("UTF-8"))

  private def decodeBase64(input: String): String = new String(Base64.getUrlDecoder.decode(input), "UTF-8")

  private def sign(data: String, secret: String): String = {
    val hmac = Mac.getInstance(algorithm)
    val secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm)
    hmac.init(secretKeySpec)
    Base64.getUrlEncoder.withoutPadding.encodeToString(hmac.doFinal(data.getBytes("UTF-8")))
  }

  def createToken(number: String): String = {
    val header = JwtHeader().asJson.noSpaces
    val payload = JwtPayload(number, System.currentTimeMillis() / 1000 + 3600).asJson.noSpaces
    val signature = sign(s"${encodeBase64(header)}.${encodeBase64(payload)}", secretKey)
    s"${encodeBase64(header)}.${encodeBase64(payload)}.$signature"
  }

  def validateToken(token: String): Boolean = {
    val parts = token.split("\\.")
    if (parts.length != 3) return false

    val header = decodeBase64(parts(0))
    val payload = decodeBase64(parts(1))
    val signature = parts(2)

    val expectedSignature = sign(s"${parts(0)}.${parts(1)}", secretKey)
    if (signature != expectedSignature) return false

    decode[JwtPayload](payload) match {
      case Right(jwtPayload) =>
        jwtPayload.exp > (System.currentTimeMillis() / 1000)
      case Left(_) => false
    }
  }

  def getNumber(token: String): Option[String] = {
    val parts = token.split("\\.")
    if (parts.length != 3) return None

    val payload = decodeBase64(parts(1))
    decode[JwtPayload](payload) match {
      case Right(jwtPayload) => Some(jwtPayload.number)
      case Left(_) => None
    }
  }
}
