package co.beetroot

sealed trait ReadFileError

case object MissingPathArg extends ReadFileError

case class NotDirectory(error: String) extends ReadFileError

case class FileNotFound(path: String) extends ReadFileError
