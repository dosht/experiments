import me.prettyprint.hector.api.query._
import github.joestein.skeletor.Cassandra
import github.joestein.skeletor.Keyspace
import github.joestein.skeletor.ColumnFamily
import github.joestein.skeletor.Conversions._
import github.joestein.skeletor.Rows

object Main extends App with Cassandra {
  Cassandra connect ("hello-cassandra", "localhost:9160")

  val ksp = Keyspace("social_scala")
  //  ksp.create
  //  val columnFamily = ColumnFamily(ksp, "User")
  val userCF = ksp \ "user"
  //  userCF.create

  // Insert
  val x = (userCF -> "dosht")
  val y = x.has("name").of("mostafa")
  val row = (userCF -> "dosht" has "name" of "mostafa")
  val rows = Rows(row)
  Cassandra << rows

  // Read
  userCF >> (
    mgsq =>
      mgsq.setKeys("dosht").setColumnNames("name"),
    (key, name, value) =>
      println(s"key: ${key}, name: ${name}, value: ${value}"))
  
  Cassandra.cluster.getConnectionManager().shutdown()
}