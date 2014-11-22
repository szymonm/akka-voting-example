name := """voting-games"""

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-remote" % "2.3.7",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.7",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)

