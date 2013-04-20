import akka.actor._

/**
 * An Actor that echoes everything to the sender
 */
class EchoActor extends Actor {
  def receive = {
    case msg => {
      sender ! msg
    }
  }
}

/**
 * An Actor that forwards everything the next actor
 */
class ForwardingActor(next: ActorRef) extends Actor {
  def receive = {
    case msg => next ! msg
  }
}

/**
 * An Actor that send only string messages
 */
class FilteringActor(next: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      next ! msg
    case _ => None
  }
}

/**
* An actor that sends a sequence of messages with a random head list, an interesting value and a random tail list
* The idea is that you would like to test that the interesting value is received and that you can't be bothered with the rest
*/
class SequencingActor(next: ActorRef, head: List[String], tail: List[String]) extends Actor {
  def receive = {
    case msg => {
      head map (next ! _)
      next ! msg
      tail map (next ! _)
    }
  }
}