name := "Akka Pi Calculator Remote"

version := "1.0"

scalaVersion := "2.10.0"

sbtVersion := "0.12.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
    "se.scalablesolutions.akka" % "akka-remote" % "1.1.3"
  )
