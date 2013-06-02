package dosht.examples.mahout

import java.io.File
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import scala.collection.JavaConversions._

object MahoutFirst extends App {
  val ratingsFilePath = "data/ratings.dat"
  val model = new FileDataModel(new File(ratingsFilePath))
  val similarity = 	new PearsonCorrelationSimilarity(model)
  val neighborhood = new NearestNUserNeighborhood(2, similarity, model)
  val recomender = new GenericUserBasedRecommender(model, neighborhood, similarity)
  val recomindations = recomender.recommend(1, 2)
  recomindations.foreach(println)
}