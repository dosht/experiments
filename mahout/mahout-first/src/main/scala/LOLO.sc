object LOLO {
  val xs = List(1, 2, 3, 4, 5, 6, 7, 8, 9)        //> xs  : List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  def bsearch(xs: List[Int], x: Int): Int = {
    def search(l: List[Int], i: Int): Int = l match {
      case head :: Nil if head == x => i
      case head :: tail if head == x => i
      case head :: tail if head != x => search(tail, i + 1)
      case _ => -1
    }
    search(xs, 0)
  }                                               //> bsearch: (xs: List[Int], x: Int)Int
	5 == xs(bsearch(xs, 5))                   //> res0: Boolean = true
	def map[A, B](xs: List[A], f: A => B): List[B] = {
		def doMap(xs: List[A], acc: List[B]): List[B] = xs match {
			case Nil => acc
			case head :: tail => doMap(tail, f(head) :: acc)
		}
		doMap(xs, Nil)
	}                                         //> map: [A, B](xs: List[A], f: A => B)List[B]
	def f(x: Int) = x * 2                     //> f: (x: Int)Int
	map(xs, (x: Int) => Math.pow(x, 2))       //> res1: List[Double] = List(81.0, 64.0, 49.0, 36.0, 25.0, 16.0, 9.0, 4.0, 1.0)
                                                  //| 
	case class X[A](xs: List[A]) {
		def ==>[B] (f: A => B): List[B] = map(xs, f)
	  def map2() = xs.map(x => x)
	}
  implicit def listToX[A](xs: List[A]) = X(xs)    //> listToX: [A](xs: List[A])LOLO.X[A]
  xs ==> f                                        //> res2: List[Int] = List(18, 16, 14, 12, 10, 8, 6, 4, 2)
  Iterable(List(1,2), List(3,4)).reduce(_:::_)    //> res3: List[Int] = List(1, 2, 3, 4)
  implicit def intToRandom(left: Float) = new {
  	def +-(right: Float) = left + right - new java.util.Random().nextFloat() + right * 2
  }                                               //> intToRandom: (left: Float)AnyRef{def +-(right: Float): Float}
  3.0f +- 0.2f                                    //> res4: Float = 3.2978413
}