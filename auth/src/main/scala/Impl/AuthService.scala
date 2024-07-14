package Impl

import Models.User
import API.{RegisterRequest, LoginRequest, AuthResponse}
import Utils.JWTUtil

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AuthService {
  private val users = mutable.Map[String, String]()

  def register(request: RegisterRequest): Future[AuthResponse] = Future {
    if (users.contains(request.username)) {
      throw new Exception("User already exists")
    } else {
      users.put(request.username, request.password)
      AuthResponse(JWTUtil.createToken(request.username))
    }
  }

  def login(request: LoginRequest): Future[AuthResponse] = Future {
    users.get(request.username) match {
      case Some(password) if password == request.password =>
        AuthResponse(JWTUtil.createToken(request.username))
      case _ =>
        throw new Exception("Invalid username or password")
    }
  }
}
