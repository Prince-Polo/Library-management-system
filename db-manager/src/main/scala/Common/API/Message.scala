package Common.API

import cats.effect.IO

trait Message[A] {
  def send(implicit connection: java.sql.Connection): IO[A]
}
