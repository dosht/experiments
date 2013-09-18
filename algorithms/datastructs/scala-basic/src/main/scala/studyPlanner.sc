object Data {
  val nph = 40                                    //> nph  : Int = 40
  val npd = 20                                    //> npd  : Int = 20
  
  // at a given point
  val nah1 = 15                                   //> nah1  : Int = 15
  val nad1 = 10                                   //> nad1  : Int = 10
  
  // at another point
  val nah2 = 25                                   //> nah2  : Int = 25
  val nad2 = 10                                   //> nad2  : Int = 10
}
object studyPlanner {
  type D = Double
  // 40 h, 20 d, pspeed = 40 / 20
  def pspeed(nph: D, nd: D): D = nph / nd
  def est(nah: D)  = ??? // number of days if i keep that speed i will finish in
  pspeed(40, 20)
}