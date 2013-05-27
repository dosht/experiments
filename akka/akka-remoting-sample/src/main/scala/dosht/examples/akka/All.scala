package dosht.examples.akka

import com.typesafe.config.ConfigFactory
import akka.kernel.Bootable
import akka.actor.{ Props, ActorSystem, Actor }
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import akka.event.Logging

// Messages
sealed trait MathMsg
case class Result(z: Int) extends MathMsg
case class Add(x: Int, y: Int) extends MathMsg
case class Multibly(x: Int, y: Int) extends MathMsg

// Remote Service
class MathActor extends Actor {
  val logger = Logging(context.system, this)

  def receive = {
    case Add(x, y) =>
      logger.info(s"Received Message Add, x: $x, y: $y")
      sender ! Result(x + y)
  }
}

class Daemon extends Bootable {
  val system = ActorSystem("remoteSystem", ConfigFactory.load.getConfig("remoteSystem"))
  val mathActor = system.actorOf(Props[MathActor], "math")

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
  implicit val timeout = Timeout(5 seconds)
  implicit val execContext = system.dispatcher

  def add(x: Int, y: Int)(callback: Int => Unit) { remoteMathActor ? Add(x, y) map {case Result(z) => callback(z)} }

  def startup() {}

  def shutdown() { system.shutdown() }
}

object MathClientRunner extends App {
  val math = new MathClient
  math.add(1, 2) { z =>
    println(z)
    math.shutdown()
  }
}

// Send Local Actor for Remote Creation
class AdvancedMathActor extends Actor {
  val logger = Logging(context.system, this)

  def receive = {
    case Multibly(x, y) =>
      logger.info(s"Received Message Multibly, x: $x, y: $y")
      sender ! Result(x * y)
  }
}

class MathRemoteCreator extends Bootable {
  val system = ActorSystem("createSystem", ConfigFactory.load.getConfig("creationSystem"))
//  val system = ActorSystem("createSystem")
  val advancedMathActor = system.actorOf(Props[AdvancedMathActor], name = "math2")

  implicit val timeout = Timeout(5 seconds)
  implicit val execContext = system.dispatcher

  def multibly(x: Int, y: Int)(callback: Int => Unit) { advancedMathActor ? Multibly(x, y) map {case Result(z) => callback(z)} }

  def startup() {}

  def shutdown() { system.shutdown() }
}

object MathRemoteCreatorRunner extends App {
  val advancedMath = new MathRemoteCreator
  advancedMath.multibly(9, 10) { z =>
    println(s"Result is $z")
    advancedMath.shutdown()
    println("system is died")
  }
}
