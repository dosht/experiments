object File {
  import java.io._
  import scala.io._
import play.api.libs.json._
  
  val db = "/home/msameh/samples/backbone/backbone-first/json"
                                                  //> db  : String = /home/msameh/samples/backbone/backbone-first/json

  val x = for (file <- new File(db).listFiles)
  	yield {
  	
  	val s = Source.fromFile(file)
  	val txt = s.getLines.reduce(_ + _)
  	Json.parse(txt)
  	}                                         //> x  : Array[play.api.libs.json.JsValue] = Array({"8":{"title":"First","name":
                                                  //| "name1"},"4":{"title":"hamada"},"9":{"title":"First","name":"name1","id":"9"
                                                  //| },"5":{"title":"First","name":"name1"},"6":{"title":"First","name":"name1"},
                                                  //| "1":{"title":"hamada"},"0":{"title":"hamada"},"2":{"title":"First","name":"n
                                                  //| ame1"},"7":{"title":"First","name":"name1"},"3":{"title":"First","name":"nam
                                                  //| e1"}}, {"10":{"title":"First","name":"name1","id":"10"}})
    val x0 = x.toList(0)                          //> x0  : play.api.libs.json.JsValue = {"8":{"title":"First","name":"name1"},"4"
                                                  //| :{"title":"hamada"},"9":{"title":"First","name":"name1","id":"9"},"5":{"titl
                                                  //| e":"First","name":"name1"},"6":{"title":"First","name":"name1"},"1":{"title"
                                                  //| :"hamada"},"0":{"title":"hamada"},"2":{"title":"First","name":"name1"},"7":{
                                                  //| "title":"First","name":"name1"},"3":{"title":"First","name":"name1"}}
    val j = x0.as[JsObject]                       //> j  : play.api.libs.json.JsObject = {"8":{"title":"First","name":"name1"},"4"
                                                  //| :{"title":"hamada"},"9":{"title":"First","name":"name1","id":"9"},"5":{"titl
                                                  //| e":"First","name":"name1"},"6":{"title":"First","name":"name1"},"1":{"title"
                                                  //| :"hamada"},"0":{"title":"hamada"},"2":{"title":"First","name":"name1"},"7":{
                                                  //| "title":"First","name":"name1"},"3":{"title":"First","name":"name1"}}
	j.fieldSet.toMap                          //> res0: scala.collection.immutable.Map[String,play.api.libs.json.JsValue] = Ma
                                                  //| p(8 -> {"title":"First","name":"name1"}, 4 -> {"title":"hamada"}, 9 -> {"tit
                                                  //| le":"First","name":"name1","id":"9"}, 5 -> {"title":"First","name":"name1"},
                                                  //|  6 -> {"title":"First","name":"name1"}, 1 -> {"title":"hamada"}, 0 -> {"titl
                                                  //| e":"hamada"}, 2 -> {"title":"First","name":"name1"}, 7 -> {"title":"First","
                                                  //| name":"name1"}, 3 -> {"title":"First","name":"name1"})
	j                                         //> res1: play.api.libs.json.JsObject = {"8":{"title":"First","name":"name1"},"4
                                                  //| ":{"title":"hamada"},"9":{"title":"First","name":"name1","id":"9"},"5":{"tit
                                                  //| le":"First","name":"name1"},"6":{"title":"First","name":"name1"},"1":{"title
                                                  //| ":"hamada"},"0":{"title":"hamada"},"2":{"title":"First","name":"name1"},"7":
                                                  //| {"title":"First","name":"name1"},"3":{"title":"First","name":"name1"}}
	
}