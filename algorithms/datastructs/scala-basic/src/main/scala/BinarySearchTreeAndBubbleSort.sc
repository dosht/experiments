object Try {
  import BinarySearchTree._
  val n = Node(10, Node(8, Node(7), Node(9)), Node(11))
                                                  //> n  : BinarySearchTree.Node = Node(10,Node(8,Node(7,null,null),Node(9,null,nul
                                                  //| l)),Node(11,null,null))
  find(9, n)                                      //> res0: BinarySearchTree.Node = Node(9,null,null)
  insert(1, n)
  n                                               //> res1: BinarySearchTree.Node = Node(10,Node(8,Node(7,Node(1,null,null),null),
                                                  //| Node(9,null,null)),Node(11,null,null))
  find(1, n)                                      //> res2: BinarySearchTree.Node = Node(1,null,null)
  test(n)                                         //> res3: Boolean = true
  del(1, n)
  find(1, n)                                      //> res4: BinarySearchTree.Node = null
  test(n)                                         //> res5: Boolean = true
  import BubbleSort._
  val xs = List(6, 7, 9, 8, 4, 5, 2, 1, 3)        //> xs  : List[Int] = List(6, 7, 9, 8, 4, 5, 2, 1, 3)
  sort(xs)                                        //> res6: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  sort2(xs, Nil)                                  //> res7: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
  sort3(xs, Nil, Nil, xs.length)                  //> res8: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
}