package dosht.cassandra.examples

import com.netflix.astyanax.AstyanaxContext
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl
import com.netflix.astyanax.connectionpool.NodeDiscoveryType
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor
import com.netflix.astyanax.thrift.ThriftFamilyFactory
import com.netflix.astyanax.model.ColumnFamily
import com.netflix.astyanax.serializers.StringSerializer
import java.util.UUID
import scala.collection.JavaConversions._

object SocialEngine {
  val context = new AstyanaxContext.Builder()
    .forCluster("social_app")
    .forKeyspace("social_engine_astyanax")
    .withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE))
    .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
      .setPort(9160)
      .setMaxConnsPerHost(10)
      .setSeeds("127.0.0.1:9160"))
    .withConnectionPoolMonitor(new CountingConnectionPoolMonitor)
    .buildKeyspace(ThriftFamilyFactory.getInstance)

  context.start()
  val keyspace = context.getEntity
  val users = new ColumnFamily("Users", StringSerializer.get, StringSerializer.get)
  val posts = new ColumnFamily("Posts", StringSerializer.get, StringSerializer.get)
  val comments = new ColumnFamily("Comments", StringSerializer.get, StringSerializer.get)
  val postRatings = new ColumnFamily("PostRatings", StringSerializer.get, StringSerializer.get)
  val userRatings = new ColumnFamily("UserRatings", StringSerializer.get, StringSerializer.get)
  val ratingCounter = new ColumnFamily("RatingsCounter", StringSerializer.get, StringSerializer.get)

  def getUser(username: String) =
    (for (x <- keyspace.prepareQuery(users).getKey(username).execute.getResult.iterator)
      yield (x.getName -> x.getStringValue)) toMap

  def addUser(username: String, name: String, email: String) = {
    val m = keyspace.prepareMutationBatch
    m.withRow(users, username)
      .putColumn("name", name)
      .putColumn("email", email)
    m.execute()
    username
  }

  def getPost(postKey: String) =
    (for (x <- keyspace.prepareQuery(posts).getKey(postKey).execute.getResult.iterator)
      yield (x.getName -> x.getStringValue)) toMap

  def addPost(username: String, content: String) = {
    val postKey = UUID.randomUUID.toString
    println(postKey)
    val m = keyspace.prepareMutationBatch
    m.withRow(posts, postKey)
      .putColumn("username", username)
      .putColumn("content", content)
    m.execute()
    postKey
  }

  def getComments(postKey: String) =
    (for (x <- keyspace.prepareQuery(comments).getKey(postKey).execute.getResult.iterator)
      yield (x.getName -> x.getStringValue)) toMap

  def addComment(postKey: String, username: String, comment: String) = {
    val m = keyspace.prepareMutationBatch
    m.withRow(comments, postKey)
      .putColumn(username, comment)
    m.execute()
    postKey
  }

  def getUserRating(username: String, postKey: String) =
    (for (
      x <- keyspace.prepareQuery(userRatings).getKey(username)
        .withColumnSlice(postKey).execute.getResult.iterator // or List(postKey, ...)
    ) yield (x.getName -> x.getLongValue)) toMap

  def getPostRating(postKey: String) =
    (for (x <- keyspace.prepareQuery(ratingCounter).getKey(postKey).execute.getResult.iterator)
      yield (x.getName -> x.getLongValue)) toMap

  def getPostRatingDetailed(postKey: String) =
    (for (x <- keyspace.prepareQuery(postRatings).getKey(postKey).execute.getResult.iterator)
      yield (x.getName -> x.getLongValue())) toMap

  def ratePost(postKey: String, username: String, rating: Int) = {
    val m = keyspace.prepareMutationBatch
    m.withRow(userRatings, username).putColumn(postKey, rating)
    m.withRow(postRatings, postKey).putColumn(username, rating)
    m.withRow(ratingCounter, postKey).incrementCounterColumn("total", rating).incrementCounterColumn("count", 1)
    m.execute()
  }
}

object SocialEngineRunner extends App {
  /*
   from the cassandra cli
   create keyspace social_engine_astyanax;
   use social_engine_astyanax;
   create column family Users;
   create column family Posts;
   create column family Comments;
   create column family PostRatings;
   create column family UserRatings;
   create column family RatingsCounter with default_validation_class=CounterColumnType and replicate_on_write=true;
   */

  SocialEngine.addUser("dosht", "mostafa", "dosht@gmail.com")
  val user = SocialEngine.getUser("dosht")
  println(s"user created: ${user}")

  val postKey = SocialEngine.addPost("dosht", "this is my first post")
  val post = SocialEngine.getPost(postKey)
  println(s"post created: ${post}")

  SocialEngine.addComment(postKey, "dosht1", "like1")
  val comment = SocialEngine.getComments(postKey)
  println(s"comment added: ${comment}")

  SocialEngine.ratePost(postKey, "dosht", 3)
  val postRating = SocialEngine.getPostRating(postKey)
  println(s"postRating: ${postRating}")
  val userRating = SocialEngine.getUserRating("dosht", postKey)
  println(s"userRating: ${userRating}")
  val postRatingDetailed = SocialEngine.getPostRatingDetailed(postKey)
  println(s"postRatingDetailed: ${postRatingDetailed}")
}