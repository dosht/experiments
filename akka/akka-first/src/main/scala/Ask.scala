import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

class AskActor extends Actor {
  def receive = {
    case "exit" => context.system.shutdown()
    case msg => sender ! s"Actor replies: $msg"
  }
}

object Ask extends App {
  val system = ActorSystem("AskSystem")
  val askActor = system.actorOf(Props[AskActor], name = "ask-actor")
  implicit val timeout = Timeout(5 seconds)
  implicit val executionContext = system.dispatcher
  (askActor ? "welcome").map (println)
  askActor ! "exit"
}
