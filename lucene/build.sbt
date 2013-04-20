name := "Similarity Recomender"

version := "1.0"

scalaVersion := "2.10.0"

sbtVersion := "0.12.0"

libraryDependencies ++= Seq(
  "org.apache.lucene" % "lucene-core" % "4.2.0",
  "org.apache.lucene" % "lucene-queries" % "4.2.0",
  "org.apache.lucene" % "lucene-suggest" % "4.2.0",
  "org.apache.lucene" % "lucene-classification" % "4.2.0"
)
