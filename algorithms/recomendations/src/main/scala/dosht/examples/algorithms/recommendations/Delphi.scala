package dosht.examples.algorithms.recommendations

import scala.xml.Xhtml

object Delphi {
  def calculateRatingCountMatrix = ???
  def agreementCount(user1: User, user2: User, nRatingValues: Int) = ???
}

class CFRecommender(user: User)(implicit x: Int) {
  def recommend = println(x)
}

object DelphiRunner extends App {
  implicit def toCFRecommender(user: User) = new CFRecommender(user)
  implicit val x = 889
  val u = new User(1, "dosht", Map())
  u.recommend
}