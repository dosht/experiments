object BubbleSort {
  def min(x: Int, y: Int) = if (x < y) x else y
  def max(x: Int, y: Int) = if (x > y) x else y
//  def sort(xs: List[Int]): List[Int] = xs match {
//    case Nil => Nil
//    case x :: Nil => x :: Nil
//    case x :: y :: tail => min(x, y) :: sort(max(x, y) :: tail)
//  }
  def sort2(xs: List[Int], acc: List[Int]): List[Int] = xs match {
    case Nil => acc
    case x :: Nil => sort2(acc, Nil) ::: List(x)
    case x :: y :: tail => sort2(max(x, y) :: tail, acc ::: List(min(x, y)))
  }
  def sort3(xs: List[Int], acc1: List[Int], acc2: List[Int], ln: Int): List[Int] =
    if (acc2.length == ln) acc2
    else
      xs match {
        case Nil => acc1
        case x :: Nil => sort3(acc1, Nil, x :: acc2, ln)
        case x :: y :: tail => sort3(max(x, y) :: tail, min(x, y) :: acc1, acc2, ln)
      }
  def sort(xs: List[Int]): List[Int] = sort3(xs, Nil, Nil, xs.length)
}