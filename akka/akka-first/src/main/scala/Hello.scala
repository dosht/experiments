import akka.actor.{ Actor, ActorSystem, Props }
import scala.concurrent.duration._

class HelloActor extends Actor {
  def receive = {
    case "exit" => context.system.shutdown()
    case message => println(message)
  }
}

object Hello extends App {
  val system = ActorSystem("HelloWorld")
  val helloActor = system.actorOf(Props[HelloActor], name = "hello-actor")
  helloActor ! "hi actor"
  helloActor ! "exit"
}
