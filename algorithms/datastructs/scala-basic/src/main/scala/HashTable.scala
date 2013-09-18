object HashTable {
  def hash(v: String)(implicit a: Array[String]) = v.## % a.size
  def put(v: String)(implicit a: Array[String]) = {
    val k = hash(v)
    a(k) = v
    k
  }
  def get(k: Int)(implicit a: Array[String]) = a(k)
  def find(v: String)(implicit a: Array[String]) = hash(v)
}