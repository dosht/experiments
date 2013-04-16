package models

import akka.actor._
import scala.concurrent.duration._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Future
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import scala.collection.mutable.{ Map => MutableMap }
import play.api.libs.concurrent.Akka

case class Task(id: Long, label: String)

object Task {

  //  Akka.system.scheduler.scheduleOnce(10 seconds) {
  //    println(100.toString())
  //  }

  def print(initVal: Int): Unit = {
    Akka.system.scheduler.scheduleOnce(10 seconds) {
      println(s"Message from iteration: $initVal")
    }
        
    Thread.sleep(10 * 1000)
    print(initVal + 1)
  }
  new Thread() {
    override def run() {
      print(0)
    }
  }.start()

  //  Akka.system.scheduler.schedule(0 seconds, 10 seconds)(println("hi"))

  val taskActor = Akka.system.actorOf(Props[TaskActor], name = "taskactor")
  val tasks: MutableMap[Long, Task] = MutableMap.empty
  var x = 1

  def all: List[Task] = tasks.foldLeft(List[Task]())(_ :+ _._2)

  def create(label: String) = {
    tasks put (x, Task(x, label))
    x += 1
  }

  def get(id: Long): Option[Task] = {
    tasks get id
  }

  def delete(id: Long) = {
    tasks remove id
  }

}

class TaskActor extends Actor with ActorLogging {
  var tasks: List[Task] = List.empty
  var x = 1
  log.info("UserManager Created:" + this.self.path)
  private[this] implicit val timeout = Timeout(1 second)

  def receive = {
    case GetTasks => sender ! tasks
    case DeleteTasks(id) => tasks = tasks.filterNot(_.id == id)
    case CreateTask(id, label) => {
      tasks = tasks :+ Task(x, label)
      x += 1
    }
  }

}

case object GetTasks
case class DeleteTasks(id: Long)
case class CreateTask(id: Long, label: String)