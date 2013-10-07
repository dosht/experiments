package dosht.examples.akka

import akka.actor.TypedActor
import akka.actor.ActorSystem
import akka.actor.TypedProps
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import akka.actor.Props

trait Calculator {
  def add(x1: Int, x2: Int): Int
  def asyncAdd(x1: Int, x2: Int): Future[Int]
}

class CalculatorImpl extends Calculator {
  def add(x1: Int, x2: Int) = x1 + x2
  def asyncAdd(x1: Int, x2: Int) = Future(x1 + x2)
}

class CalculatorImpl2 extends Calculator {
  def add(x1: Int, x2: Int) = (x1 + x2) * 100
  def asyncAdd(x1: Int, x2: Int) = Future((x1 + x2) * 100)
}

object TypedCalculator extends App {
  val system = ActorSystem("typed-actor")
  val typedSystem = TypedActor(system)
  val calculator1: Calculator = typedSystem.typedActorOf(TypedProps[CalculatorImpl]())
  val calculator2: Calculator = typedSystem.typedActorOf(TypedProps[CalculatorImpl]())
  val calculator3: Calculator = typedSystem.typedActorOf(TypedProps[CalculatorImpl2]())
  println("start")
  println(calculator1.add(1, 2))
  calculator2.asyncAdd(1, 4) map println
  calculator3.asyncAdd(1, 4) map println
  system.shutdown()
  println("end")
}

//object TypedCalculatorFor extends App {
//  val system = ActorSystem("typed-actor")
//  val typedSystem = TypedActor(system)
//}