object BinarySearchTree {
  case class Node(val k: Int, var l: Node = null, var r: Node = null)
  def test(n: Node): Boolean =
    if (n == null) true
    else if (n.l != null && n.l.k > n.k) false
    else if (n.r != null && n.r.k < n.k) false
    else test(n.l) && test(n.r)

  def find(k: Int, n: Node): Node =
    if (n == null || n.k == k) n
    else if (k < n.k) find(k, n.l)
    else find(k, n.r)

  def _insert(n0: Node, n: Node): Unit =
    if (n == null || n0 == null) Unit
    else if (n0.k < n.k && n.l == null) n.l = n0
    else if (n0.k > n.k && n.r == null) n.r = n0
    else if (n0.k < n.k) _insert(n0, n.l)
    else _insert(n0, n.r)

  def insert(k: Int, n: Node) = _insert(Node(k), n)
  trait Direction
  case object Left extends Direction
  case object Right extends Direction
  def del(k: Int, n: Node): Unit = {
    def _del(k: Int, n: Node, d: Direction, p: Node): Unit =
      if (n == null) Unit
      else if (k == n.k) {
        d match {
          case Left => p.l = null
          case Right => p.r = null
        }
        _insert(n.l, p); _insert(n.r, p)
      } else if (k < n.k) _del(k, n.l, Left, n)
      else _del(k, n.r, Right, n)
    _del(k, n, null, null)
  }
}