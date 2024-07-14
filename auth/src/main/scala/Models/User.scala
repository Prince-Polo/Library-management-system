package Models

import io.circe.generic.JsonCodec

@JsonCodec case class User(username: String, password: String)
