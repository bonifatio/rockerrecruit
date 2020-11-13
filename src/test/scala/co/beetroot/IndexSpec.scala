package co.beetroot

import org.scalatest.{FlatSpec, Matchers}

class IndexSpec extends FlatSpec with Matchers {

  behavior of "index updates"

  it should "update index properly with new data" in {
    val index = Index(Map.empty)

    val index1 = index.updated("test1.txt", Seq("foo" -> 2, "bar" -> 3))

    index1 shouldBe
      Index(lookup = Map(
        "foo" -> Seq(WordStats(2, "test1.txt")),
        "bar" -> Seq(WordStats(3, "test1.txt"))
      ))

    index1.updated("test2.txt", Seq("baz" -> 4, "foo" -> 1)) shouldBe
      Index(lookup = Map(
        "foo" -> Seq(WordStats(2, "test1.txt"), WordStats(1, "test2.txt")),
        "bar" -> Seq(WordStats(3, "test1.txt")),
        "baz" -> Seq(WordStats(4, "test2.txt"))
      ))

  }

}