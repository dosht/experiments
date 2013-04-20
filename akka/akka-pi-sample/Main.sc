object Main {
  var acc = 0.0                                   //> acc  : Double = 0.0
  for (i <- 1 until 10)
    acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
  acc                                             //> res0: Double = -0.9581603810705976
  
  val acc2 = (for (i <- 1 until 10)
  	yield 4.0 * (1 - (i % 2) * 2) / (2 * i + 1))
  	.foldLeft(0.0)(_+_)                       //> acc2  : Double = -0.9581603810705976
  acc2 == acc                                     //> res1: Boolean = true
  
}