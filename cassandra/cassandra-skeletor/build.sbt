name := "Cassandra Skeletor"

scalaVersion := "2.10.1"

sbtVersion := "0.12.0"

libraryDependencies ++= Seq (
  "me.prettyprint" % "hector-core" % "1.0-3",
  "org.apache.cassandra" % "cassandra-all" % "1.0.8",
  "org.slf4j" % "slf4j-log4j12" % "1.6.4"
)
