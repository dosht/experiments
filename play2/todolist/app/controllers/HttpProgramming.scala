package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import play.api.libs.iteratee.Enumerator
import scala.concurrent.Future

object Application extends Controller {

  val taskForm = Form("label" -> nonEmptyText)

  def index = Action {
    Redirect(routes.Application.tasks)
  }

  // REST
  def tasks = Action {
    Ok(views.html.index(Task.all, taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all, errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      })
  }

  def getTask(id: Long) = Action {
    Task.get(id) match {
      case Some(task) => Ok(s"<h1>${task.label}</h1>").as(HTML)
      case None => NotFound("NotFound")
    }
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  // Headers and routing
  def json = Action(parse.json) { implicit request =>
    val ret = s"${request.body \ "something"}"
    println(ret)
    Ok(ret)
  }

  def simpleResult = Action {
    SimpleResult(
      header = ResponseHeader(200, Map(CONTENT_TYPE -> "text/plain")),
      body = Enumerator("Hi, ", "hi again"))
  }

  def files(name: String) = Action {
    Ok(s"downloading: $name")
  }

  def withHeaders = Action {
    implicit val charSet = Codec.javaSupported("iso-8859-1")
    Ok("something")
      .withHeaders(
        CONTENT_TYPE -> HTML(charSet),
        CACHE_CONTROL -> "max-age=3600",
        ETAG -> "xx")
      .withCookies(
        Cookie("xx", "yy"))
  }

  // Sessions
  val loginForm = Form("username" -> nonEmptyText())

  def login() = Action { implicit request =>
    request.method match {
      case "POST" => loginForm.bindFromRequest.fold(
        errors => BadRequest(views.html.login(errors)),
        username => Redirect(routes.Application.home).withSession("username" -> username)).flashing("action" -> "logged in")
      case "GET" => request.session.get("username") match {
        case None => Ok(views.html.login(loginForm))
        case Some(_) => Redirect(routes.Application.home).flashing("action" -> "already logged in")
      }
    }
  }

  def home = Action { implicit request =>
    //    implicit val flash: Flash
    Ok(views.html.home())
  }

  def logout = Action { request =>
    //    Redirect(routes.Application.home).withSession(request .session - "username")
    Redirect(routes.Application.home).withNewSession.flashing("action" -> "logged out")
  }

  // Body Parser
  def upload = Action(parse.multipartFormData) { implicit request =>
    // curl -POST "http://localhost:9000/upload" --form image=xyz.jpg 
    request.body.file("image").map { image =>
      Ok(views.html.image(image.ref.file.getCanonicalPath()))
    }.getOrElse(BadRequest("FileNotFound"))
  }

  def ActionWithLogging[A](bp: BodyParser[A] = parse.empty)(call: Request[A] => Result): Action[A] = {
    Action(bp) { request =>
      Logger.debug(s"Request -> $request")
      val result = call(request)
      Logger.error(s"${result.getClass()}")
      Logger.debug(s"Response <- $result")
      result
    }
  }

  def loggable(id: Long, username: String) = ActionWithLogging(parse.empty) { result =>
    Ok("hi")
  }

  case class User(name: String)
  val authenticatedUsers = Map("231fdcc12" -> User("dosht"), "231fdcc13" -> User("mostafa"))
  case class AuthenticatedRequest[A](
    user: User, private val request: Request[A]) extends WrappedRequest(request)

  def Authenticated[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = {
    Action(p) { request =>
      val result = for {
        id <- request.session.get("user")
        user <- authenticatedUsers.get(id)
      } yield f(AuthenticatedRequest(user, request))
      result getOrElse Unauthorized
    }
  }

  //    def cpanel = Authenticated { implicit request: Result[String] =>
  //        Ok("Welcome " + request.user.name)
  //    }  //TODO: fix cpanel

  // Async
  def async = Action {
    import play.api.libs.concurrent.Execution.Implicits._
    import scala.concurrent.duration._
    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Oops", 2 seconds)
    Async {
      Future {
        var i = 0
        while (i < 10) {
          Thread.sleep(1000)
          i += 1
          println(s"iteration $i")
        }
        i
      }.map(i => Ok(s"result is $i"))
    }
  }
}
