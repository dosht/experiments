package dosht.titan.examples

import org.apache.commons.configuration.BaseConfiguration
import com.thinkaurelius.titan.core.TitanFactory
import org.apache.log4j.Logger
import org.apache.log4j.Level
import java.lang.{ Integer => JInteger, Long => JLong }
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.Direction._
import scala.collection.JavaConversions._
import java.util.Date

object SimpleTwitter {
  def format(user: Vertex) = user.toString() + " " + user.getProperty("name")
  def now = new Date().getTime
  val logger = Logger.getLogger("Simple Twitter")
  val conf = new BaseConfiguration()
  conf.setProperty("storage.backend", "cassandra")
  conf.setProperty("storage.hostname", "127.0.0.1")
  val g = TitanFactory.open(conf)
  logger.info(s"Opened Graph: ${g}")

  // Create vertices types
  val timeType = g.makeType.name("time").dataType(classOf[JLong]).functional.makePropertyKey()
  g.makeType.name("text").dataType(classOf[String]).functional.makePropertyKey()
  g.makeType.name("name").dataType(classOf[String]).indexed.unique.functional.makePropertyKey()

  // Create edges types
  //  val timeType = g.makeType.name("time").simple.functional(false).dataType(classOf[JInteger]).makePropertyKey()
  g.makeType.name("follows").primaryKey(timeType).makeEdgeLabel()
  g.makeType.name("tweets").primaryKey(timeType).makeEdgeLabel()
  g.makeType.name("streams").primaryKey(timeType).unidirected.makeEdgeLabel()
  logger.info("Types created")

  def addUser(username: String) = {
    val user = g.addVertex(null)
    user.setProperty("name", username)
    logger.info(s"Added user: ${format(user)}")
    user
  }

  def follow(follower: Vertex, follwee: Vertex) = {
    val time = now
    val edge = g.addEdge(null, follower, follwee, "follows")
    edge.setProperty("time", time)
    logger.info(s"Added follow relation: ${format(follower)} -> ${format(follwee)}")
    edge
  }

  def tweet(user: Vertex, tweet: String) = {
    val time = now
    val vertex = g.addVertex(null)
    vertex.setProperty("text", tweet)
    vertex.setProperty("time", time)
    val edge = g.addEdge(null, user, vertex, "tweets")
    edge.setProperty("time", time)
    logger.info(s"Added tweet: ${tweet} user: ${format(user)}")
    user.getVertices(IN, "follows").iterator.foreach {
      i =>
        val x = g.addEdge(null, i, vertex, "streams")
        logger.info(s"Added tweet to stream, tweet: ${tweet}, user: ${format(i)}")
        x.setProperty("time", time)
    }
    (vertex, edge)
  }

  def getStream(user: Vertex) = user.getVertices(OUT, "streams").toList
  
  def getRecommended(user: Vertex) = {
    val followees = user.getVertices(OUT, "follows").slice(0, 10).toList
    // TODO: complete the recommendation function based on the followees
  }
}

object SimpleTwitterRunner extends App {
  class User(me: Vertex) {
    def -> (other: Vertex) = SimpleTwitter.follow(me, other)
    def tweet(tweet: String) = SimpleTwitter.tweet(me, tweet)
    def stream = SimpleTwitter.getStream(me).foldLeft(List[String]()) {
      (list, i) => list :+ i.getProperty("text")
    }
  }
  implicit def toFolower(v: Vertex) = new User(v)

  val dosht = SimpleTwitter.addUser("dosht")
  val sara = SimpleTwitter.addUser("sara")
  val amal = SimpleTwitter.addUser("amal")
  val salma = SimpleTwitter.addUser("salma")
  val noha = SimpleTwitter.addUser("noha")

  dosht -> amal
  sara -> amal
  amal -> salma
  amal -> noha

  amal tweet "hello"

  println(dosht stream)
  println(amal stream)
  
}