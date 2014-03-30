/*** SimpleApp.scala ***/
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.scheduler.SplitInfo

object SimpleApp {
  //  val sparkHome = Option(System getenv "SPARK_HOME") getOrElse "../incubator-spark/"
  //  val jarFile = "target/scala-2.10/spark-naive-bayes_2.10-0.1-SNAPSHOT.jar"
  //
  //  val logFile = s"$sparkHome/README.md" // Should be some file on your system
  //  val sc = new SparkContext("local", "Naive Bayes", sparkHome, List(jarFile), Map(), Map())
  //
  //  val logData = sc.textFile(logFile, 2).cache()
  //  val numAs = logData.filter(line => line.contains("a")).count()
  //  val numBs = logData.filter(line => line.contains("b")).count()
  //  println(Console.CYAN)
  //  println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  //  println(Console.RESET)
  //
  //  sc.stop()

  // P(c | w) = P(w | c) * P(c) / P(w)
  def main(args: Array[String]) {
    println(Console.CYAN_B)
    //	  println(vocabList(data))
    println(bagOfWords(data.posts(0)))
    println(Console.RESET)

  }

  val data = Data(List(
    "my dog dog has flea problems help please",
    "maybe not take him to dog park stupid",
    "my dalmation is so cute I love him",
    "stop posting stupid worthless garbage",
    "mr licks ate my steak how to stop him",
    "quit buying worthless dog food stupid"),
    List(0, 1, 0, 1, 0, 1))

  def vocabList(data: Data): Set[String] = data.posts.flatMap(p => p.split(" ")).toSet // P(W)

  def bagOfWords(text: String): Map[String, Int] = text.split(" ").foldLeft(Map[String, Int]())((xs, x) => // P(W | C)
    xs + (xs get x map (c => x -> (c + 1)) getOrElse (x -> 1)))

}

case class Data(posts: List[String], classes: List[Int])