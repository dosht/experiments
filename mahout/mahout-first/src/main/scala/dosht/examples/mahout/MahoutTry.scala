package dosht.examples.mahout.boolean

import org.apache.mahout.cf.taste.impl.common.FastByIDMap
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray
import org.apache.mahout.cf.taste.model.PreferenceArray
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel
import org.apache.mahout.cf.taste.impl.model.GenericDataModel
import org.apache.mahout.cf.taste.impl.model.BooleanPreference
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import scala.collection.JavaConversions._
import Math._

object DB {
  // Helpers
  type UserID = Int
  type ItemID = Int
  var currentId = 0
  def _newId = {
    currentId += 1
    currentId
  }
  def rand(x: Int) = new java.util.Random().nextInt(x)
  val r = 100

  // Algorithms
  // -- items
  def genItemList(n: Int, cat: Cat): List[Item] = (1 to n).map(i => Item(_newId, s"$cat$i", cat)).toList
  def orderedItems(prefs: Prefs, n: Int): List[ItemID] =
    prefs.map(p => catToItems(p.cat).take((p.weight / prefs.sumWeights * n).toInt).map(_.id)).reduce(_ ::: _)
  def randomItems(prefs: Prefs, n: Int): List[ItemID] = ???
  def takeRandom[A](list: Traversable[A]): Traversable[A] = ???

  // -- users
  def genRandomPrefs(cats: Seq[Cat]): Prefs = new Prefs(cats.foldLeft((cats.length, List[Pref]())) {
    case ((boost, prefs), cat) => (boost - 1, Pref(cat, rand(5) + boost * 10) :: prefs)
  }._2)
  def genPersonList(n: Int, m: Int, name: String, cats: Seq[Cat])(setItems: (Prefs, Int) => List[Int]): List[Person] = {
    val prefs = genRandomPrefs(cats)
    (1 to n).map(i => Person(_newId, s"${name}", prefs, setItems(prefs, m))).toList
  }
  lazy val developers = genPersonList(1000, 10, "developer", Development :: Paint :: Music :: Nil)(orderedItems)
  lazy val painters = genPersonList(1000, 10, "painters", Paint :: Development :: Nil)(orderedItems)
  lazy val musicians = genPersonList(1000, 10, "musicians", Music :: Music :: Sport :: Nil)(orderedItems)
  lazy val athletics = genPersonList(1000, 10, "athletics", Sport :: Nil)(orderedItems)

  // -- general
  def sample(persons: List[Person]): Map[UserID, List[ItemID]] = persons.map(p => p.id -> p.items).toMap

  // Items
  lazy val devList: List[Item] = genItemList(r, Development)
  lazy val paintList: List[Item] = genItemList(r, Paint)
  lazy val musicList: List[Item] = genItemList(r, Music)
  lazy val sportList: List[Item] = genItemList(r, Sport)
  lazy val catToItems: Map[Cat, List[Item]] = Map(
    Development -> devList,
    Paint -> paintList,
    Music -> musicList,
    Sport -> sportList)
  lazy val items: Map[ItemID, Item] = catToItems.values.map(x => x.map(x => x.id -> x).toMap).reduce(_ ++ _)

  // Users
  //  lazy val users: List[Person] = List(Person(1, "developer", Prefs(Development -> 10, Paint -> 3, Sport -> 1, Music -> 6), Nil))
  lazy val users: Map[UserID, Person] = (developers ::: painters ::: musicians ::: athletics).map(u => u.id -> u).toMap
}

case class Item(id: Int, name: String, cat: Cat)
case class Person(id: Int, name: String, prefs: Prefs, items: List[Int])
trait Cat
case object Development extends Cat
case object Paint extends Cat
case object Music extends Cat
case object Sport extends Cat
case class Pref(cat: Cat, weight: Float)
class Prefs(prefs: TraversableOnce[Pref]) {
  override def toString() = s"Prefs(${prefs.map(p => s"${p.cat} -> ${p.weight}").reduce(_ + ", " + _)})"
  def apply(cat: Cat) = Pref(cat,
    prefs.filter(_.cat == cat) match {
      case Nil => 0
      case p => p.toList(0).weight
    })
  def map[B](f: Pref => B): TraversableOnce[B] = prefs.map(f)
  val weights = map(_.weight)
  val sumWeights = weights.sum
}
case object Prefs {
  def apply(elems: (Cat, Int)*) = new Prefs(elems.map(e => Pref(e._1, e._2)))
}

object ManualTry extends App {
  def sim(u1: Person, u2: Person) = ???
  val u = DB.users.values.head
  val vs = DB.users.values.tail
  val is = DB.items.values.filterNot(i => u.items.exists(_ == i.id))
  for {
    i <- is
    v <- vs
    r <- v.items.find(_ == i)
  } yield sim(u, v)
}

object MahoutTry extends App {
  def counts(xs: List[Int]) = Map(
    Development -> xs.count(id => DB.items(id).cat == Development),
    Sport -> xs.count(id => DB.items(id).cat == Sport),
    Paint -> xs.count(id => DB.items(id).cat == Paint),
    Music -> xs.count(id => DB.items(id).cat == Music))
  println("Devers: " + counts(DB.developers(0).items))
  println("Painters: " + counts(DB.painters(0).items))
  println("Musicians: " + counts(DB.musicians(0).items))
  println("Athletics: " + counts(DB.athletics(0).items))

  // Calculating some recommendations
  val prefs = new FastByIDMap[PreferenceArray]()
  DB.users.values.foldLeft(0) { (i, user) =>
    val prefsPerUser = new GenericUserPreferenceArray(user.items.length)
    prefsPerUser.setUserID(i, user.id)
    user.items.foldLeft(0) { (j, item) =>
      prefsPerUser.setItemID(j, item)
      j + 1
    }
    prefs put (user.id, prefsPerUser)
    i + 1
  }
  val model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(prefs))
  //  val model = new GenericDataModel(prefs)
  val similarity = new LogLikelihoodSimilarity(model)
  val neighborhood = new ThresholdUserNeighborhood(100, similarity, model)
  val recommender = new GenericUserBasedRecommender(model, neighborhood, similarity)
  recommender.recommend(DB.athletics(0).id, 10).foreach(r => DB.items(r.getItemID.toInt))
}