name := "auth"

version := "0.1"

scalaVersion := "3.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "com.typesafe.akka" %% "akka-stream" % "2.6.14",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",
  "de.heikoseeberger" %% "akka-http-circe" % "1.35.2",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "com.github.jwt-scala" %% "jwt-core" % "9.0.1",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)
