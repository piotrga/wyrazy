package googletranslate

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.collection.immutable._

class TranslatorTest extends FlatSpec with ShouldMatchers{
  object PL2EN extends Translator(from = "pl", to = "en", apiKey = "AIzaSyBA45xu5yZDal-p49_wKu8fLsmMAMQ6uuU")

  implicit def int2ListBuilder(src : Int) = new ListBuilder(src)


  "EN2PL" should "translate one word" in{
    PL2EN.translate(List("slowo")) should be (List("word"))
  }

  it should "translate multiple words" in{
    PL2EN.translate(List("slowo", "ptak")) should be (List("word", "bird"))
  }

  it should "translate big batches of words" in{
    PL2EN.translate(100 times "slowo") should be (100 times "word")
  }

  class ListBuilder(repeat:Int){
    def times[T](element : T) = for (i<- 1 to repeat) yield element
  }


}





