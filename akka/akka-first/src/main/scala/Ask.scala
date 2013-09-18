import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import akka.dispatch.OnFailure

class AskActor extends Actor {
  def receive = {
    case "exit" => context.system.shutdown()
    case "give me error" => throw new Exception("something wrong happened")
    case msg => sender ! s"Actor replies: $msg"
  }
}

object Ask extends App {
  val system = ActorSystem("AskSystem")
  val askActor = system.actorOf(Props[AskActor], name = "ask-actor")
  implicit val timeout = Timeout(5 seconds)
  implicit val executionContext = system.dispatcher
  (askActor ? "give me error").onFailure { case e => println(e) }
  (askActor ? "some message").onSuccess { case m => println(m) }
  for {
    f1 <- (askActor ? "message1").mapTo[String]
    f2 <- (askActor ? "message2").mapTo[String]
  } println(f1 + f2)
  (askActor ? "welcome").mapTo[String] map (println)
  askActor ! "exit"
}
