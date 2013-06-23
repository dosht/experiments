package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.io._
import scala.io._

object Application extends Controller {
  // Database
  var database = Map[String, Map[String, JsValue]]()
  val db = "json"
  load()

  // DB Helpers
  implicit def StringToFile(filename: String) = new {
    def <<(str: String) = {
      val p = new PrintWriter(new File(filename))
      try p.write(str)
      finally p.close()
    }
  }

  def save() {
    database.foreach {
      case (collection, jsonMap) =>
        s"$db/$collection" << JsObject(jsonMap.toSeq).toString //TODO: user Json.prettyPrint
    }
  }

  def load() {
    if (new File(db).isDirectory) database = readStoredJson
    println(database)
  }

  def readStoredJson(): Map[String, Map[String, JsValue]] =
    new File(db).listFiles.map(f =>
      f.getName -> Json.parse(Source.fromFile(f).getLines.reduce(_ + _)).as[JsObject].fieldSet.toMap).toMap

  sys.addShutdownHook {
    save()
  }

  var currentId = -1

  def newId = {
    currentId += 1
    currentId
  }

  def collection(implicit name: String) = database.get(name).getOrElse {
    val coll = Map[String, JsValue]()
    database += (name -> coll)
    coll
  }

  def js(implicit request: Request[AnyContent]) = request.body.asJson.toList(0)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def post(implicit collName: String) = Action {
    implicit request =>
      val id = s"$newId"
      database += (collName -> (collection + (id -> js)))
      save()
      println(s"${request.method}: $collName/ -> $id\n\t [$collName]: $collection")
      Ok(Json.obj("id" -> id))
  }

  def getAll(implicit collName: String) = Action {
    implicit request =>
      println(s"${request.method}: $collName/\n\t [$collName]: $collection")
      Ok(Json.arr(collection))
  }

  def get(id: String, collName: String) = Action {
    implicit request =>
      implicit val name = collName
      println(s"${request.method}: $collName/$id\n\t [$collName]: $collection")
      collection.get(id).map(Ok(_)).getOrElse(NotFound)
  }

  def put(id: String, collName: String) = Action {
    implicit request =>
      implicit val name = collName
      database += (collName -> (collection + (id -> js)))
      save()
      println(s"${request.method}: $collName/$id\n\t [$collName]: $collection")
      Ok(Json.obj("id" -> id))
  }

  def delete(id: String, collName: String) = Action {
    request =>
      implicit val name = collName
      database += (collName -> (collection - id))
      save()
      println(s"${request.method}: $collName/$id\n\t [$collName]: $collection")
      Ok(id)
  }
}