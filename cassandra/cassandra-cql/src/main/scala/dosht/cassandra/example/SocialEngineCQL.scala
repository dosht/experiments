package dosht.cassandra.example

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.BoundStatement
import scala.collection.JavaConversions._

object SocialEngineCQL {
  lazy val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect()
  session.execute("""
      CREATE KEYSPACE social_engine_cql WITH replication
      {'class': 'SimpleStrategy', 'replication_factor': 3};
  """)
  session.execute("""
      CREATE TABLE users (
		  username text PRIMARY KEY,
		  name text,
		  email text)
  """)

  def addUser(username: String, name: String, email: String) =
    new BoundStatement(session prepare "INSERT INTO users (username, name, email) values (?, ?, ?)")
      .bind(username, name, email)

  def getUser(username: String) =
    (for (x <- session.execute(s"SELECT * FROM users WHERE username = '${username}'").iterator)
      yield x)

  def close() = cluster.shutdown()
}

object SocialEngineCQLRunner extends App {
  SocialEngineCQL.addUser("dosht", "mostafa", "dosht@gmail.com")
  val user = SocialEngineCQL.getUser("dosht")
  println(user)
}