name := "Cassandra Astyanax"

scalaVersion := "2.10.1"

sbtVersion := "0.12.0"

libraryDependencies ++= Seq(
  "com.netflix.astyanax" % "astyanax-core" % "1.56.37",
  "com.netflix.astyanax" % "astyanax-thrift" % "1.56.37",
  "com.netflix.astyanax" % "astyanax-cassandra" % "1.56.37"
)
