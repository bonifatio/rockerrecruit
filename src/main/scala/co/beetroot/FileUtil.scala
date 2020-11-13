package co.beetroot

import java.io.File
import java.nio.file.Files

import scala.io.Source
import scala.jdk.CollectionConverters.IteratorHasAsScala
import scala.util.{Try, Using}

object FileUtil {

  def readDirectory(args: Array[String]): Either[ReadFileError, File] = {
    for {
      file <- Try { new java.io.File(args.head) }.toEither.left map { _ => MissingPathArg }
      _ <- Either.cond(file.exists(), (), FileNotFound(file.getPath))
      _ <- Either.cond(file.isDirectory, (), NotDirectory(s"Path [${file.getPath}] is not a directory"))
    } yield file
  }

  def listFilesRecursively(directory: File): Seq[File] = {
    Files
      .walk(directory.toPath).iterator.asScala
      .filter(Files.isRegularFile(_))
      .map(_.toFile)
      .toSeq
  }

  def readFile(file: File): Seq[String] = {
    Using.resource(Source.fromFile(file, "UTF-8")) {
      _.getLines().toSeq
    }
  }
}
