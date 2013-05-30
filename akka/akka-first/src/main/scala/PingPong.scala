import akka.actor._

class Ping(pong: ActorRef) extends Actor {
  var i = 0
  var n = 10
  def inc { i += 1; println(s"Ping $i") }
  def receive = {
    case "start" =>
      inc
      pong ! "ping"
    case "pong" =>
      inc
      if (i < n) sender ! "ping"
      else {
        sender ! "stop"
        Thread.sleep(1000)
        println("stop ping")
        context.stop(self)
      }
  }
}

class Pong extends Actor {
  def receive = {
    case "ping" =>
      println("Pong")
      sender ! "pong"
    case "stop" =>
      println("stop pong")
      context.stop(self)
      println("SHUTDOWN")
      context.system.shutdown()
  }
}

object PingPong extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "pong")
  ping ! "start"
}
