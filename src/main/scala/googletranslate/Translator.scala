package googletranslate

import java.net.URLEncoder
import scala.io.Source
import scala.util.parsing.json.JSON

class Translator(from:String, to:String, apiKey: String){
  val baseUrl = "https://www.googleapis.com/language/translate/v2?key="+apiKey+"&source="+from+"&target="+to

  def translate(phrases:Seq[String]) = {
    val chunked = phrases.grouped(20).toList
    chunked.map(translateFew(_)).flatten
  }

  def translateFew(phrases:Seq[String]) = {
    val url = baseUrl + phrases.map("&q=" + URLEncoder.encode(_, "UTF-8")).mkString
    val response = Source.fromURL(url, "UTF-8").mkString
    JSON.parseFull(response) match{
      case Some(response : Map[String, Map[String, List[Map[String,String]]]]) => response("data")("translations").map( _("translatedText"))
      case x => throw new RuntimeException("Unexpected response format: " + response)
    }
  }
}

