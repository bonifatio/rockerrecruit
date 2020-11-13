package co

package object beetroot {
  type Word = String

  final case class WordStats(occurences: Int, fileName: String)

  final case class FileScore(fileName: String, score: Double)
}