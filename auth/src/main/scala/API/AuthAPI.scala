package API

import io.circe.generic.JsonCodec

@JsonCodec case class RegisterRequest(username: String, password: String)
@JsonCodec case class LoginRequest(username: String, password: String)
@JsonCodec case class AuthResponse(token: String)
