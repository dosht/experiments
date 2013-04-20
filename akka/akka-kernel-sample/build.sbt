name := "Akka Kernel Sample"

version := "1.0"

scalaVersion := "2.10.0"

sbtVersion := "0.12.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.typesafe.akka" % "akka-kernel_2.10.0-RC1" % "2.1.0-RC1"
)