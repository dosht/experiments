package dosht.titan.scala

import com.ansvia.graph.BlueprintsWrapper._
import org.apache.commons.configuration.BaseConfiguration
import com.thinkaurelius.titan.core.TitanFactory
import com.tinkerpop.blueprints.Direction.{ OUT, IN, BOTH }
import scala.collection.JavaConversions._
import com.ansvia.graph.annotation.Persistent

// model
case class Person(name: String, email: String) extends DbObject

case class Animal(name: String, count: Int, total: Int) extends DbObject{
  @Persistent val avg = if (count < 1) 0 else total / count
}

object TitanScalaExample extends App {
  // config and open db
  val conf = new BaseConfiguration()
  conf.setProperty("storage.backend", "cassandra")
  conf.setProperty("storage.hostname", "127.0.0.1")
  implicit lazy val g = TitanFactory.open(conf)

  val liza = Person("liza", "liza@gmail.com").save()
  val heba = Person("heba", "heba@gmail.com").save()
  val lizaObject = liza.toCC[Person].get
  println(lizaObject)
  println(liza.getProperty("name"))

  liza <-- "mother" <-- heba
  liza --> "doughter" --> heba

  val mothers = liza.getVertices(OUT, "doughter")
  mothers.foreach(x => println(s"the mother of liza is: ${x.toCC[Person]}"))

  val lala = Person("lala", "lala@gmail.com").save()
  val lalaId = lala.getId
  transact {
    val friendship = (liza --> "friend" --> lala <)
    friendship.set("kind", "follows")
  }
  g.getVertex(lalaId).getEdges(IN, "friend").foreach(x =>
    println(s"kind of friendship from liza to lala: ${x.get("kind")}"))
    
  // More complex
  Person("ihab", "ihab@hotmail.com").save()
  Person("ihab", "ihab@yahoo.com").save()
  Person("ihab", "ihab@gmail.com").save()
  Person("ihab", "ihab@mail.com").save()
//  val people = g.query().has("name", "ihab").limit(10).vertices
  val people = g.query().has("name", "ihab").has("email", "ihab@gmail.com").limit(10).vertices
  people.foreach(l => println(l.toCC[Person]))
  
  Animal("zarafa", 10, 40).save()
  val avg = g.query().has("name", "zarafa").vertices.toList(0).toCC[Animal].get.avg
  println(avg)
}