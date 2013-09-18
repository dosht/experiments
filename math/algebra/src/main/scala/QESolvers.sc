import Math.sqrt
object QESolvers {
  def abs(x: Float) = if (x < 0) -x else x        //> abs: (x: Float)Float
  def factorial(x: Float) = (for (i <- 1 to x.toInt) yield i).filter(i => x % i == 0).map(i => (x / i, i.toFloat))
                                                  //> factorial: (x: Float)scala.collection.immutable.IndexedSeq[(Float, Float)]
  def solve(a: Float, b: Float, c: Float) =
    factorial(abs(c))
      .filter(i => if (b > 0) (i._1 + i._2) == b else abs(i._1 - i._2) == b)
                                                  //> solve: (a: Float, b: Float, c: Float)scala.collection.immutable.IndexedSeq[(
                                                  //| Float, Float)]
  def solve2(a: Float, b: Float, c: Float) = {
    val s = sqrt(b * b - 4 * a * c)
    val a2 = 2 * a
    val ans1 = (-b + s) / a2
    val ans2 = (-b - s) / a2
    if (ans1 != ans2) (ans1, ans2) else (ans1)
  }                                               //> solve2: (a: Float, b: Float, c: Float)Any
  solve2(2, 5, -3)                                //> res0: Any = (0.5,-3.0)
	sqrt(317)                                 //> res1: Double = 17.804493814764857
  solve(1, -10, 24)                               //> res2: scala.collection.immutable.IndexedSeq[(Float, Float)] = Vector()
}