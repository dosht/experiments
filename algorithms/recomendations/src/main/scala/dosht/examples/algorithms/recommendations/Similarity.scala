/**
 * Assume we have users and items. Users can like items.
 * So for each user the system should show recommended items
 * depends on items liked by other users who liked similar items to that user.
 */
package dosht.examples.algorithms.recommendations

import scala.io.Source
import Math._

case class Item(id: Int, name: String, ratings: Map[Int, Rating]) {
  def averageRating: Double = ratings.foldLeft(0.0)(_ + _._2.rating) / ratings.size
  def commonUserIds(that: Item): Iterable[Int] = for (r <- ratings.values if that.ratings.isDefinedAt(r.userId)) yield r.userId
  def ratingsForItemList(userIds: Iterable[Int]): Iterable[Int] = for (uid <- userIds) yield ratings(uid).rating
}

case class Rating(userId: Int, itemId: Int, rating: Int)

case class User(id: Int, name: String, ratings: Map[Int, Rating]) {
  def getAverageRating: Double = ratings.foldLeft(0.0)(_ + _._2.rating) / ratings.size
  def commonItemIds(that: User): Iterable[Int] = for (r <- ratings.values if that.ratings.isDefinedAt(r.itemId)) yield r.itemId
  def ratingsForItemList(itemIds: Iterable[Int]): Iterable[Int] = for (iid <- itemIds) yield ratings(iid).rating
  def similarity1(that: User): Double = // tanh(sum(pow(r1-r2, 2)-n))
    (for {
      r1 <- this.ratings.values
      r2 <- that.ratings.values
      if (r1.itemId == r2.itemId)
    } yield pow(r1.rating - r2.rating, 2)) match {
      case Nil => 0.0d
      case rs => 1.0d - tanh(sqrt(rs.sum / rs.size))
    }
  // another way - imperative
  //    var sim = 0.0d
  //    var commonItems = 0
  //    for (r <- this.ratings.values) {
  //      for (r2 <- that.ratings.values) {
  //        if (r.itemId == r2.itemId) {
  //          commonItems += 1
  //          sim += Math.pow((r.rating - r2.rating), 2)
  //        }
  //      }
  //    }
  //    if (commonItems > 0) {
  //      sim = Math.sqrt(sim / commonItems)
  //      sim = 1.0d - Math.tanh(sim)
  //    }
  //    sim
  //  }

  def similarity2(that: User): Double =
    (for {
      r1 <- this.ratings.values
      r2 <- that.ratings.values
      if (r1.itemId == r2.itemId)
    } yield pow(r1.rating - r2.rating, 2)) match {
      case Nil => 0.0d
      case rs => 1.0d - tanh(sqrt(rs.sum / rs.size)) * (rs.size / min(this.ratings.size, that.ratings.size))
    }
}

object Test extends App {
  //	Data.users.foreach { user =>
  //	  println {
  //	    s"${user.name}:\t${user.ratings.map(r => Data.movies.filter(i => i.id == r._2.itemId).map(_.name))}\n"
  //	  }
  //	}
  //  println(Data.users(0).similarity(Data.users(3)))
  Data.users.foreach { u1 =>
    Data.users.foreach { u2 =>
      println(s"""${u1.name} and ${u2.name}:	${u1.similarity2(u2)}""")
    }
  }
}

object Data {
  val usersDataFileName = "data/user_ratings/users.dat"
  lazy val users = Source.fromFile(usersDataFileName).getLines.foldLeft(List[User]()) {
    (users, line) =>
      val tokens = line.mkString.split("::")
      if (tokens.length == 2) {
        val uid = tokens(0).toInt
        users :+ User(uid, tokens(1), (for (r <- ratings if r.userId == uid) yield (r.itemId -> r)) toMap)
      } else users
  }

  val moviesDataFileName = "data/user_ratings/movies.dat"
  lazy val movies = Source.fromFile(moviesDataFileName).getLines.foldLeft(List[Item]()) {
    (items, line) =>
      val tokens = line.mkString.split("::")
      if (tokens.length == 2) {
        val iid = tokens(0).toInt
        items :+ Item(iid, tokens(1), (for (r <- ratings if r.itemId == iid) yield (r.userId -> r)) toMap)
      } else items
  }

  val ratingsDataFileName = "data/user_ratings/ratings.dat"
  lazy val ratings = Source.fromFile(ratingsDataFileName).getLines.foldLeft(List[Rating]()) {
    (ratings, line) =>
      val tokens = line.mkString.split("::")
      if (tokens.length == 3)
        ratings :+ Rating(tokens(0).toInt, tokens(1).toInt, tokens(2).toInt)
      else ratings
  }
}
