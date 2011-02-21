import googletranslate.Translator
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{ BorderLayout}
import javax.swing._
import scala.collection.immutable._


object PL2EN extends Translator("pl", "en", "AIzaSyBA45xu5yZDal-p49_wKu8fLsmMAMQ6uuU")    {
  def translate2map(words:List[String]): List[(String,String)] = {
    words.zip(translate(words))
  }
}

object Wyrazy{

  def analyzeText(text: String) = {
    var words = Set(text.split("[^a-zA-ZąęłńóśćżźĄĆĘŁŃŚŻŹ]"): _*)
    //    var words = Set(text.split("[\\s\\.,!;\\-\\?\"\\(\\)/\u201D]"):_*)

    words = words.map(_.toLowerCase)
    words = words.filter(_.length > 1)
    words = words.filter(!_.matches("[0-9].*"))

    val sortedWords: Array[String] = TreeSet(words.toArray: _*).toArray

    removeSimilar(sortedWords.reverse)
  }


  def prettyPrintResults(removed: List[(String, String)], words: List[String]): String = {
    val translatedWords = PL2EN.translate2map(words)
    var res = ""
    res = res + "Removed:\n"
    removed.foreach((x:Pair[String, String]) =>res = res + x.toString + "\n")
    res = res + "----------------------------\n"
    res = res + "Words:\n"
    translatedWords.foreach((x:Pair[String,String])=>res = res + x._1 +" -> "+ x._2 + "\n")
    res = res + "============================\n"
    res = res + "Words: " + words.size + "\n"
    res = res + "Removed: " + removed.size + "\n"
    res
  }

  def main(args: Array[String]){

    val window = new JFrame
    window.setLayout(new java.awt.BorderLayout)

    val textArea: JTextArea = new JTextArea("Wklej text")

    val button = new  JButton("Oblicz")
    val result = new JTextArea("Result")
    button.addActionListener( new ActionListener(){
      def actionPerformed(p1: ActionEvent) = result setText calculate(textArea.getText)
    } )
    val split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(textArea), new JScrollPane(result) )
    window.getContentPane.add(button, BorderLayout.NORTH)
    window.getContentPane.add(split, BorderLayout.CENTER)
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    window.setSize(800,600)
    window.show
  }


  def calculate(text:String) = {
    try {
      val (words, removed) = analyzeText(text)
      prettyPrintResults(removed, words)
    } catch {
      case e: Throwable => e.toString
    }
  }

  def removeSimilar(sortedWords:Array[String]) = {
    var res = List(sortedWords(0))
    var removed: List[Pair[String, String]] = Nil

    for (i <- 1 to sortedWords.length - 1) {
      val previous = sortedWords(i - 1)
      val current = sortedWords(i)

      if (!similar(previous, current))
        res = res.::(current)
      else
        removed = removed.::((current, previous))
    }
    (res, removed)
  }

  def similar(a:String, b :String):Boolean = {
    val len:Int = Math.max(a.length, b.length) * 7 / 10
    if(Math.max(a.length, b.length) <= 4) return false
    if (len > Math.min(a.length, b.length)) return false;
    a.substring(0,len).toLowerCase.equals(b.substring(0,len).toLowerCase)
  }
}