package googletranslate

import googletranslate.Translator
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.collection.immutable._

class TranslatorTest extends FlatSpec with ShouldMatchers{
  object PL2EN extends Translator("pl", "en", "AIzaSyBA45xu5yZDal-p49_wKu8fLsmMAMQ6uuU")

  "EN2PL" should "translate one word" in{
    PL2EN.translate(List("slowo")) should be (List("word"))
  }
  it should "translate multiple words" in{
    PL2EN.translate(List("slowo", "ptak")) should be (List("word", "bird"))
  }

  it should "translate big batches of words" in{
    PL2EN.translate(repeat("slowo",  100)) should be (repeat("word",  100))
  }

  def repeat(word: String, num: Int) = {
    for (i<- 1 to num) yield word
  }
}





