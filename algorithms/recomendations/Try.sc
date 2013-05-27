import scala.io.Source

object Try {
  case class R(value: Int)
  val xs = Map(1 -> R(10), 2-> R(20))             //> xs  : scala.collection.immutable.Map[Int,Try.R] = Map(1 -> R(10), 2 -> R(20)
                                                  //| )
  xs.isDefinedAt(1)                               //> res0: Boolean = true
  xs.foldLeft(0) {
    (total, item) =>
      total + item._2.value
  } / xs.size                                     //> res1: Int = 15
  
  xs.foldLeft(0)(_ + _._2.value) / xs.size        //> res2: Int = 15
  (0.0 /: xs) (_ + _._2.value) / xs.size          //> res3: Double = 15.0
  
  case class C(id: Int, name: String)
  val ys = List(C(1, "a"), C(2, "b"), C(3, "c"))  //> ys  : List[Try.C] = List(C(1,a), C(2,b), C(3,c))
  (for (y <- ys) yield (y.id -> y.name)) toMap    //> res4: scala.collection.immutable.Map[Int,String] = Map(1 -> a, 2 -> b, 3 -> 
                                                  //| c)
  val (as, bs) = (List(1,2,3), List(5,6,2))       //> as  : List[Int] = List(1, 2, 3)
                                                  //| bs  : List[Int] = List(5, 6, 2)
  val som = (for {
    a <- as
    b <- bs
    if a == b
  } yield (a, b)) map { case (x, y) => x * y }    //> som  : List[Int] = List(4)
  som match {
    case Nil => 0
    case is => 1
  }                                               //> res5: Int = 1
}