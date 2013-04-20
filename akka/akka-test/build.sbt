name := "Akka Test"

version := "1.0"

scalaVersion := "2.10.0"

sbtVersion := "0.12.0"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.typesafe.akka" % "akka-testkit" % "2.0.5",
  "org.scalatest" % "scalatest_2.8.1" % "1.5"
)
