name := "Akka Remote Sample"

version       := "0.1"

scalaVersion  := "2.10.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/", 
  "maven repo" at "http://scala-tools.org/repo-snapshots",
  "akka repo" at "http://repo.akka.io/snapshots/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.typesafe.akka" %% "akka-remote" % "2.1.2",
  "com.typesafe.akka" %% "akka-kernel" % "2.1.2",
  "log4j" % "log4j" % "1.2.14"
)
