package dosht.examples.mahout.rating

import scala.util.Random
import org.apache.mahout.cf.taste.impl.model.GenericDataModel
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray
import org.apache.mahout.cf.taste.impl.common.FastByIDMap
import org.apache.mahout.cf.taste.model.PreferenceArray
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import scala.collection.JavaConversions._
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import scala.io.Source

object ImplicitConversions {
  implicit def intToRandom(x: Int) = new {
    def +-(y: Int) = if (y > 0) x + y - new java.util.Random().nextInt(y * 2) else x
  }
  implicit def floatToRandom(x: Float) = new {
    def +-(y: Float) = x + y - new java.util.Random().nextFloat() + y * 2
  }
  implicit def filter(xs: List[{ val id: Int }]) = new {
    def takeRandom(count: Int, range: Int) = {
      val mod = xs.length / count + 1
      xs.filter(_.id % mod == 0 +- range)
    }
  }
}
object DB {
  import ImplicitConversions._

  // Random items Map cat -> items without ratings
  val itemsCounts: Map[ItemCat, Int] = Map(Development -> 50, Paint -> 50, Music -> 50, Sport -> 50)
  def genItems(cat: ItemCat, n: Int): List[Item] = (1 to n).map(i => Item(s"$cat-$i", cat, Map[Int, Rating]())).toList
  lazy val itemsLists: Map[ItemCat, List[Item]] = itemsCounts.map { case (cat, n) => cat -> genItems(cat, n) }

  // Random users Map cat -> users without ratings
  val usersCounts: Map[UserCat, Int] = Map(Developer -> 10, Painter -> 10, Musician -> 10, Athletic -> 10)
  val usersConfig: Map[UserCat, Prefs] = Map(
    Developer -> Prefs(Development -> 10, Paint -> 5, Music -> 3, Sport -> 1),
    Painter -> Prefs(Paint -> 10, Development -> 3, Music -> 1),
    Musician -> Prefs(Music -> 10, Sport -> 5, Paint -> 1),
    Athletic -> Prefs(Sport -> 10, Music -> 5))
  def genUsers(cat: UserCat, prefs: Prefs, n: Int): List[User] = (1 to n).map(i => User(s"$cat-$i", prefs, Map[Int, Rating]())).toList
  lazy val usersLists: Map[UserCat, List[User]] = usersCounts.map { case (cat, n) => cat -> genUsers(cat, usersConfig(cat), n) }

  // Random users list with ratings based on user preferences
  lazy val users: Map[Int, User] = (for {
    us <- usersLists.values
    User(name, prefs, _, id) <- us
  } yield {
    val ratings = (for {
      Pref(cat, w) <- prefs.values
      item <- itemsLists.get(cat).getOrElse(Nil).takeRandom(100, 2)
    } yield (item.id -> Rating(id, item.id, ((w +- 2) / prefs.sumWeights) * 5))).toMap
    id -> User(name, prefs, ratings, id)
  }).toMap

  lazy val users2: Map[Int, User] =
    usersLists.values.flatMap(us => us.map {
      case User(name, prefs, _, id) =>
        id -> User(name, prefs, prefs.values.flatMap {
          case Pref(cat, w) =>
            itemsLists.get(cat).getOrElse(Nil).takeRandom(10, 2).map(i => (1 -> Rating(id, i.id, w)))
        }.toMap, id)
    }).toMap

  // Random items list with ratings based on user preferences
  lazy val items: Map[Int, Item] = (for {
    is <- itemsLists.values
    i <- is
  } yield {
    val Item(name, cat, _, id) = i
    val ratings = Map[Int, Rating]() //TODO: query the users collections
    id -> Item(name, cat, ratings, id)
  }).toMap
}

object Algorithms {
  import Math._
  def userSimilarity(u1: User, u2: User) = (for {
    r1 <- u1.ratings.values
    r2 <- u2.ratings.values
    if (r1.itemId != r2.itemId)
  } yield pow(r1.rating - r2.rating, 2)) match {
    case Nil => 0.0d
    case rs => 1.0d - tanh(sqrt(rs.sum / rs.size))
  }
}

object RatingsTry extends App {
  // Persintation
  //  println(DB.itemsLists)
  //  val users = DB.users.values.take(3).map {
  //    case User(name, _, ratings, _) => (name -> ratings.values.take(3)
  //      .map { case Rating(uid, iid, r) => (DB.items(iid), r) })
  //  }
  //  users.foreach(println)

  // Mahout
  val prefs = new FastByIDMap[PreferenceArray]()
  DB.users.values.foreach {
    case User(name, prefs, ratings, uid) =>
      val prefsPerUser = new GenericUserPreferenceArray(ratings.toList.length)
      prefsPerUser.setUserID(0, uid)
      ratings.values.foldLeft(0) {
        case (i, Rating(_, iid, r)) =>
          prefsPerUser.setItemID(i, iid)
          prefsPerUser.setValue(i, r)
          (i + 1)
      }
  }
  val model = new GenericDataModel(prefs)
  val similarity = new PearsonCorrelationSimilarity(model)
  //  similarity.allSimilarItemIDs(arg0)
  val neighborhood = new NearestNUserNeighborhood(1, similarity, model)
  //  for (n <- neighborhood.getUserNeighborhood(0)) println (n)
  val recommender = new GenericUserBasedRecommender(model, neighborhood, similarity)
  println(recommender.recommend(0, 10))

  // Manual
  //  println("calculating") // TODO: check use similarity are too small?
  //  val developers = DB.usersLists(Developer).map(u => DB.users(u.id))
  //  val u = developers.head
  //  val vs = List(developers.head)
  //  val is = DB.items.values.filterNot(i => u.ratings.values.exists(r => r.itemId == i.id))
  //  val neighborhood = vs.map(v => Algorithms.userSimilarity(u, v))
  //  println(neighborhood)

  //  val file = new java.io.File("gogo")
  //  import sys.process._
  //  ("cat 'fff'" #> file).!!
}
object UID {
  var currentId = -1
  def apply(): Int = {
    currentId += 1
    currentId
  }
}
case class User(name: String, prefs: Prefs, ratings: Map[Int, Rating], id: Int = UID())
case class Item(name: String, cat: ItemCat, ratings: Map[Int, Rating], id: Int = UID())
case class Rating(userId: Int, itemId: Int, rating: Float)

trait Cat

trait ItemCat extends Cat
case object Development extends ItemCat
case object Paint extends ItemCat
case object Music extends ItemCat
case object Sport extends ItemCat

trait UserCat extends Cat
case object Developer extends UserCat
case object Painter extends UserCat
case object Musician extends UserCat
case object Athletic extends UserCat

// Helpers
case class Pref(cat: ItemCat, weight: Float)
class Prefs(prefs: TraversableOnce[Pref]) {
  override def toString() = s"Prefs(${prefs.map(p => s"${p.cat} -> ${p.weight}").reduce(_ + ", " + _)})"
  def apply(cat: ItemCat) = Pref(cat,
    prefs.filter(_.cat == cat) match {
      case Nil => 0
      case p => p.toList(0).weight
    })
  def map[B](f: Pref => B): TraversableOnce[B] = prefs.map(f)
  val weights = map(_.weight)
  val sumWeights = weights.sum
  val values = prefs.toList
}
case object Prefs {
  def apply(elems: (ItemCat, Int)*) = new Prefs(elems.map(e => Pref(e._1, e._2)))
}
