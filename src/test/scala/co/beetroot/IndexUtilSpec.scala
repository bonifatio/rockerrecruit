package co.beetroot

import java.io.File

import org.scalatest.{FlatSpec, Matchers}

class IndexUtilSpec extends FlatSpec with Matchers {

  behavior of "word stats functionality"

  it should "parse lines and count words stats" in {
    val lines = Seq(" Foo,  bar baz.", "Bar baz, abcdef?  Baz, that’s. ")

    val expected = Map(
      "foo" -> 1, "bar" -> 2, "baz" -> 3, "abcdef" -> 1, "that’s" -> 1
    )

    IndexUtil.getFileStats(lines) shouldBe expected
  }

  behavior of "indexing file"

  it should "create proper index" in {

    val path = getClass.getClassLoader.getResource("test-folder").getPath
    val directory = new File(path)

    val expected = Map(
      "foo" -> Seq(WordStats(1, "file1.txt"), WordStats(1, "file2.txt"), WordStats(1, "file3.txt"), WordStats(1, "file4.txt")),
      "bar" -> Seq(WordStats(2, "file1.txt"), WordStats(2, "file2.txt"), WordStats(1, "file3.txt"), WordStats(1, "file4.txt")),
      "baz" -> Seq(WordStats(3, "file1.txt"), WordStats(3, "file2.txt"), WordStats(1, "file3.txt")),
      "abcdef" -> Seq(WordStats(1, "file1.txt"))
    )

    val (index, actualFilesNumber) = IndexUtil.index(directory)

    val actualIndex = index.lookup.view.mapValues(_.sortBy(_.fileName)).toMap

    actualIndex shouldBe expected
    actualFilesNumber shouldBe 4
  }

  it should "calculate scores for the target word for each file" in {

    val lookup = Map(
      "baz" -> Seq(WordStats(3, "file1.txt"), WordStats(4, "file2.txt"), WordStats(1, "file3.txt")),
      "abcdef" -> Seq(WordStats(1, "file1.txt"))
    )

    val actual = IndexUtil.getFileScoresByWord(lookup, "baz")

    val expected = Seq(
      FileScore("file1.txt", 0.75), FileScore("file2.txt", 1.0), FileScore("file3.txt", 0.25)
    )

    actual should contain theSameElementsAs expected
  }

  it should "calculate scores for multiple words for each file" in {

    val lookup = Map(
      "foo" -> Seq(WordStats(1, "file1.txt"), WordStats(1, "file2.txt"), WordStats(1, "file3.txt"), WordStats(1, "file4.txt")),
      "bar" -> Seq(WordStats(2, "file1.txt"), WordStats(2, "file2.txt"), WordStats(1, "file3.txt"), WordStats(1, "file4.txt")),
      "baz" -> Seq(WordStats(3, "file1.txt"), WordStats(4, "file2.txt"), WordStats(1, "file3.txt")),
      "abcdef" -> Seq(WordStats(1, "file1.txt"))
    )

    val actual = IndexUtil.getScoresByFile("bar baz", Index(lookup))

    val expected = Seq(
      FileScore("file2.txt", 100.0), FileScore("file1.txt", 87.5), FileScore("file3.txt", 37.5), FileScore("file4.txt", 25.0)
    )

    actual shouldBe expected
  }

}
