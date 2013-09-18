object HashTableTry {
  import HashTable._
  implicit val a = Array.fill(100)("")            //> a  : Array[String] = Array("", "", "", "", "", "", "", "", "", "", "", "", ""
                                                  //| , "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                                                  //|  "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
                                                  //| "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "
                                                  //| ", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
                                                  //| , "", "", "", "", "", "", "", "", "", "")
  val k = put("dosht")                            //> k  : Int = 56
  get(k)                                          //> res0: String = dosht
  find("dosht")                                   //> res1: Int = 56
  put("mostafa")                                  //> res2: Int = 29
  find("mostafa1")                                //> res3: Int = -16
  "mostafa1".##                                   //> res4: Int = -176069416

	def _fib(x: Int, i: Int, p1: Int, p2: Int, r: Int): Int =
	  x match {
	  	case 0 => 0
	  	case 1 => 1
	  	case _ =>
	  	  if (i == x) r
	  	  else _fib(x, i + 1, p2, r, p1 + p2)
	  }                                       //> _fib: (x: Int, i: Int, p1: Int, p2: Int, r: Int)Int
	def fib(x: Int) = _fib(x, 0, 0, 1, 0)     //> fib: (x: Int)Int
	  // 0 1 1 2 3 5 8
	(for (i <- 0 to 10) yield (i, fib(i)))    //> res5: scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((0,0), (1,1
                                                  //| ), (2,1), (3,1), (4,2), (5,2), (6,3), (7,4), (8,5), (9,7), (10,9))
}