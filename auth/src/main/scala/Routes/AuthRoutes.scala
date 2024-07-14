package Routes

import API.{RegisterRequest, LoginRequest, AuthAPI}
import Impl.AuthService
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._

object AuthRoutes {
  val routes: Route =
    pathPrefix("auth") {
      path("register") {
        post {
          entity(as[String]) { body =>
            decode[RegisterRequest](body) match {
              case Right(request) =>
                onComplete(AuthService.register(request).unsafeToFuture()) {
                  case scala.util.Success(response) => complete(StatusCodes.OK, response.asJson.noSpaces)
                  case scala.util.Failure(exception) => complete(StatusCodes.BadRequest, exception.getMessage)
                }
              case Left(error) => complete(StatusCodes.BadRequest, error.getMessage)
            }
          }
        }
      } ~
        path("login") {
          post {
            entity(as[String]) { body =>
              decode[LoginRequest](body) match {
                case Right(request) =>
                  onComplete(AuthService.login(request).unsafeToFuture()) {
                    case scala.util.Success(response) => complete(StatusCodes.OK, response.asJson.noSpaces)
                    case scala.util.Failure(exception) => complete(StatusCodes.BadRequest, exception.getMessage)
                  }
                case Left(error) => complete(StatusCodes.BadRequest, error.getMessage)
              }
            }
          }
        }
    }
}
