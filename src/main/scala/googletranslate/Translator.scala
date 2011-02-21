package googletranslate

import java.net.URLEncoder
import scala.io.Source

class Translator(from:String, to:String, key: String){
  import scala.util.parsing.json.JSON
  val url = "https://www.googleapis.com/language/translate/v2?key="+key+"&source="+from+"&target="+to

  def translate(phrases:Seq[String]) = {
    val chunked = phrases.grouped(20).toList
    chunked.map(translateFew(_)).flatten
  }

  def translateFew(phrases:Seq[String]) = {
    val response = Source.fromURL(url + phrases.map("&q=" + URLEncoder.encode(_)).mkString).mkString
    JSON.parseFull(response) match{
      case Some(map:Map[String, Map[String, List[Map[String,String]]]]) => map("data")("translations").map( _("translatedText"))
      case x => throw new RuntimeException("Wrong answer "+x)
    }
  }
}

