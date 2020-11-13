package co.beetroot

import java.io.File

import org.scalatest.EitherValues._
import org.scalatest.{FlatSpec, Matchers}

class FileUtilSpec extends FlatSpec with Matchers {

  behavior of "reading directory"

  it should "handle valid directory" in {
    val folder = getClass.getClassLoader.getResource("test-folder").getPath
    FileUtil.readDirectory(Array(folder)).isRight shouldBe true
  }

  it should "handle not existing directory" in {
    FileUtil.readDirectory(Array("foo")).left.value shouldBe FileNotFound("foo")
  }

  it should "handle empty/null directory" in {
    FileUtil.readDirectory(Array()).left.value shouldBe MissingPathArg
    FileUtil.readDirectory(Array(null)).left.value shouldBe MissingPathArg
  }

  it should "handle file instead of directory" in {
    val file = getClass.getClassLoader.getResource("test-folder/file1.txt").getPath
    FileUtil.readDirectory(Array(file)).left.value shouldBe a [NotDirectory]
  }

  behavior of "listing directory"

  it should "list directory files in flat format" in {
    val path = getClass.getClassLoader.getResource("test-folder").getPath
    val directory = new File(path)

    val expectedFiles = Seq(
      "/f1/file2.txt", "/f2/file3.txt", "/f2/file4.txt", "/file1.txt"
    ) map { p => new File(path + p) }

    FileUtil.listFilesRecursively(directory) shouldBe expectedFiles
  }

  behavior of "reading lines from text file"

  it should "return valid lines list" in {
    val path = getClass.getClassLoader.getResource("test-folder/file1.txt").getPath
    val file = new File(path)

    FileUtil.readFile(file) shouldBe Seq("Foo, bar baz.", "Bar baz, abcdef? Baz.")
  }
}