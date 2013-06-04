package dosht.examples.mahout

import java.io.File
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import scala.collection.JavaConversions._
import org.apache.mahout.common.RandomUtils
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.recommender.Recommender

object MahoutFirst extends App {
  println("recommender: -----------------------")
  recommend
  println("evaluation: ----------------------------")
  evaluate

  lazy val model = {
    val ratingsFilePath = "data/ratings.dat"
    new FileDataModel(new File(ratingsFilePath))
  }

  def recommender = {
    val similarity = new PearsonCorrelationSimilarity(model)
    val neighborhood = new NearestNUserNeighborhood(2, similarity, model)
    new GenericUserBasedRecommender(model, neighborhood, similarity)
  }

  def recommend {
	  recommender.recommend(1, 10).foreach(println)
  }

  def evaluate {
    RandomUtils.useTestSeed()
    val evaluater = new AverageAbsoluteDifferenceRecommenderEvaluator
    val builder = new RecommenderBuilder {
      def buildRecommender(dataModel: DataModel): Recommender = {
        recommender
      }
    }
    val score = evaluater.evaluate(builder, null, model, 0.7, 1.0)
    println(s"score: $score")
  }
}