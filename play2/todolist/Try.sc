object Try {
  import scala.collection.mutable.{ Map => MutableMap }
  val m: MutableMap[Int, String] = MutableMap.empty
                                                  //> m  : scala.collection.mutable.Map[Int,String] = Map()
  m put (1, "ggg")                                //> res0: Option[String] = None
  m.foldLeft(List[String]())(
    (all, elem) => all :+ elem._2)                //> res1: List[String] = List(ggg)
 val x = m.get(1) match {
   case None => -1
   case Some(x) => x}                             //> x  : Any = ggg
}