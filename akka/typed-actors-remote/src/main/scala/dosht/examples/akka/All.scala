package dosht.examples.akka

import com.typesafe.config.ConfigFactory
import akka.kernel.Bootable
import akka.actor.{ Props, ActorSystem, Actor }
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import akka.event.Logging
import akka.actor.TypedActor
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import akka.actor.TypedProps

// Remote Service
trait Math {
  def add(x: Int, y: Int): Future[Int]
}

class MathImpl extends Math {
  val system = TypedActor.context.system
  val logger = Logging(system, TypedActor.context.self)
  def add(x: Int, y: Int) = Future {
    logger.info(s"adding, x: $x, y: $y")
    x + y
  }
}

class Daemon extends Bootable {
  val system = ActorSystem("remoteSystem", ConfigFactory.load.getConfig("remoteSystem"))
  val tsystem = TypedActor(system)
  val mathActor = tsystem.getActorRefFor(tsystem.typedActorOf(TypedProps[MathImpl](), "math"))

  def startup() {}

  def shutdown() { system.shutdown() }
}

object DaemonRunner extends App {
  new Daemon
}

// Lookup Remote Actor
class MathClient extends Bootable {
  val system = ActorSystem("lookupSystem", ConfigFactory.load.getConfig("lookupSystem"))
  val remoteMathActor = system.actorFor("akka://remoteSystem@0.0.0.0:6001/user/math")
  val remoteMath: Math = TypedActor(system).typedActorOf(TypedProps[MathImpl], remoteMathActor)

  def add(x: Int, y: Int) = remoteMath.add(x, y)

  def startup() {}

  def shutdown() { system.shutdown() }
}

object MathClientRunner extends App {
  val math = new MathClient
  for (result ← math.add(2, 30)) {
    println(s"Result is $result")
    math.shutdown()
  }
}

// Still not working ERRRRRRRRRRRR
// Send Local Actor for Remote Creation
trait AdvancedMath {
  def multibly(x: Int, y: Int): Future[Int]
}

class AdvancedMathImpl extends AdvancedMath {
  val logger = Logging(TypedActor.context.system, TypedActor.context.self)

  def multibly(x: Int, y: Int) = Future {
    logger.info(s"multibly, x: $x, y: $y")
    x * y
  }
}

class MathRemoteCreator extends Bootable {
  val system = ActorSystem("createSystem", ConfigFactory.load.getConfig("creationSystem"))
  val typedSystem = TypedActor(system)
  
  //FIXME: ClassCastException: com.sun.proxy.$Proxy0 cannot be cast to AdvancedMathImpl
  val advancedMath = typedSystem.typedActorOf(TypedProps[AdvancedMathImpl], "math2")

  def multibly(x: Int, y: Int) = advancedMath.multibly(x, y)

  def startup() {}

  def shutdown() { system.shutdown() }
}

object MathRemoteCreatorRunner extends App {
  val advancedMath = new MathRemoteCreator
  for (result ← advancedMath.multibly(2, 30)) {
    println(s"Result is $result")
    advancedMath.shutdown()
  }
}
