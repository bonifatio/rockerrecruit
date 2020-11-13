package co.beetroot

import scala.io.StdIn.readLine

object Main extends App {

  def formatScores(scoresByFile: Seq[FileScore]): String = {
    if (scoresByFile.isEmpty) {
      "No matches found"
    } else {
      val matches = scoresByFile map { fileScore =>
        s"${fileScore.fileName}: ${fileScore.score}%"
      }

      s"Matches found: ${matches.mkString(" ")}"
    }
  }

  def iterate(indexedFiles: Index): Unit = {
    print(s"search> ")
    Console.flush()

    val searchString = readLine()

    if (searchString != ":quit") {
      val scoresByFile = IndexUtil.getScoresByFile(searchString, indexedFiles)
      println(formatScores(scoresByFile))

      iterate(indexedFiles)
    }
  }

  FileUtil
    .readDirectory(args)
    .fold(
      println,
      directory => {
        val (index, fileCount) = IndexUtil.index(directory)
        println(s"$fileCount files read in directory $directory")
        iterate(index)
      }
    )
}

