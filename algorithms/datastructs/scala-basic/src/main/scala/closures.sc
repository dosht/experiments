object closures {
  def f(p: Int) = (n: Int) => n * p               //> f: (p: Int)Int => Int
  val f2 = f(8)                                   //> f2  : Int => Int = <function1>
  val f3 = f(9)                                   //> f3  : Int => Int = <function1>
  Map('sara -> (f2(2) + f3(1)),
    'heba -> (f2(4) + f3(2)))
    .foreach(x => println(s"${x._1}\t->\t${x._2}"))
                                                  //> 'sara	->	25
                                                  //| 'heba	->	50
  def v(p: Int, f: (Int, Int) => Int) = (n: Int) => f(n, p)
                                                  //> v: (p: Int, f: (Int, Int) => Int)Int => Int
  val v1 = v(5, _ * _)                            //> v1  : Int => Int = <function1>
  v1(2)                                           //> res0: Int = 10
  List(0, 1, 1, 2, 3, 5, 8, 13, 21)               //> res1: List[Int] = List(0, 1, 1, 2, 3, 5, 8, 13, 21)
  def _fib(x: Int, i: Int, p1: Int, p2: Int): Int =
    if (i == x) p2 else _fib(x, i + 1, p2, p1 + p2)
                                                  //> _fib: (x: Int, i: Int, p1: Int, p2: Int)Int
  def fib(x: Int): Int = x match {
    case 0 => 0
    case _ => _fib(x, 1, 0, 1)
  }                                               //> fib: (x: Int)Int
  fib(9)                                          //> res2: Int = 34
  //or (i <- 0 to 10) println(fib(i))
  def _isPrime(x: Int, i: Int, r: Boolean): Boolean =
    if (!r) r else if (i < 2) r else _isPrime(x, i - 1, x % i != 0)
                                                  //> _isPrime: (x: Int, i: Int, r: Boolean)Boolean
  def isPrime(x: Int) = _isPrime(x, x - 1, true)  //> isPrime: (x: Int)Boolean
  //(1 to 100) map (x => println(x, isPrime(x)))
}