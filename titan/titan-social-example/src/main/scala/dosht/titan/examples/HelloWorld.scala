package dosht.titan.examples

import com.thinkaurelius.titan.core.TitanFactory
import scala.collection.JavaConversions._
import org.apache.commons.configuration.BaseConfiguration

object HelloWorld extends App {
  val conf = new BaseConfiguration()
  conf.setProperty("storage.backend", "cassandra")
  conf.setProperty("storage.hostname", "127.0.0.1")
  val g = TitanFactory.open(conf)
  val juno = g.addVertex(null)
  juno.setProperty("name", "juno")
  val jupiter = g.addVertex(null)
  jupiter.setProperty("name", "jupiter")
  val merried = g.addEdge(null, juno, jupiter, "married")

  for (x <- juno.query.labels("married").vertices)
    println(x.getProperty("name"))
}