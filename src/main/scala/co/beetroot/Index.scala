package co.beetroot

final case class Index(lookup: Map[String, Seq[WordStats]]) {

  def updated(fileName: String, words: Iterable[(String, Int)]): Index = {
    val newLookup = words.foldLeft(lookup) { case (currIndex, (word, count)) =>
      val existingWordStats = currIndex.getOrElse(word, Seq.empty)
      currIndex.updated(word, existingWordStats :+ WordStats(count, fileName))
    }

    Index(newLookup)
  }
}
