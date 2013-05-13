name := "Titan Scala Example"

version := "1.0"

scalaVersion := "2.10.1"

sbtVersion := "0.12.0"

resolvers += "Ansvia Releases Repo" at "http://scala.repo.ansvia.com/releases/"

libraryDependencies ++= Seq(
  "com.ansvia.graph" %% "blueprints-scala" % "0.1.0",
  "com.thinkaurelius.faunus" % "faunus" % "0.1.1"
)

