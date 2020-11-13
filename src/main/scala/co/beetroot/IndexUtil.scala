package co.beetroot

import java.io.File

object IndexUtil {

  def getFileStats(lines: Seq[String]): Map[String, Int] = {

    val words =
      for {
        l <- lines
        word <- l.toLowerCase.split("[^a-z’']") if !word.isBlank
      } yield {
        word
      }

    words
      .groupMapReduce(identity)(_ => 1)(_ + _)
  }

  def index(directory: File): (Index, Int) = {
    import FileUtil._

    val files = listFilesRecursively(directory)

    val index = files.foldLeft(Index(Map.empty)) { (m, file) =>
      val lines = readFile(file)
      m.updated(file.getName, IndexUtil.getFileStats(lines))
    }

    (index, files.length)
  }

  def getFileScoresByWord(lookup: Map[Word, Seq[WordStats]], word: String): Seq[FileScore] = {
    lookup.get(word) match {
      case Some(list) =>
        val maxCount = list.map(_.occurences).max.toDouble
        list.map(ws => FileScore(ws.fileName, ws.occurences / maxCount))

      case None =>
        Seq.empty
    }
  }

  def getScoresByFile(searchString: String,
                      indexedFiles: Index,
                      maxItems: Int = 10): Seq[FileScore] = {

    val targetWords = searchString.trim.split(' ').toSeq
    val wordsNumber = targetWords.length

    val scoresByWord =
      for {
        word <- targetWords
        wordScore <- getFileScoresByWord(indexedFiles.lookup, word)
      } yield {
        wordScore
      }

    scoresByWord
      .groupMapReduce(_.fileName)(v => v.score)(_ + _)
      .toSeq
      .map { case (fileName, totalScore) =>
        val percentage = math.ceil(10000 * totalScore / wordsNumber) / 100
        FileScore(fileName, percentage)
      }
      .sortBy(_.score)(Ordering[Double].reverse)
      .take(maxItems)
  }
}
