package dosht.examples.mahout

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import java.io.File

object MahoutEvaluator extends App {
  val file = new File("data/intro.csv")
  val model = new FileDataModel(file)
  val evaluator = new GenericRecommenderIRStatsEvaluator
  val recommenderBuilder = new RecommenderBuilder {
    def buildRecommender(dataModel: DataModel): Recommender = {
      val similarity = new PearsonCorrelationSimilarity(model)
      val neighborhood = new NearestNUserNeighborhood(2, similarity, model)
      new GenericUserBasedRecommender(model, neighborhood, similarity)
    }
  }
  val stats = evaluator.evaluate(recommenderBuilder, null, model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0)
  println(s"Precision: ${stats.getPrecision}")
  println(s"Recall: ${stats.getRecall}")
}
