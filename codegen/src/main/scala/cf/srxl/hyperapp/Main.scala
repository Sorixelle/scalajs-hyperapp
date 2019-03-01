package cf.srxl.hyperapp

import java.io.File
import java.nio.file.Files

import scala.collection.JavaConverters._

object Main {
  def main(args: Array[String]): Unit = args.toList match {
    case path :: tags :: Nil if path.endsWith(".scala") =>
      println(s"Generating tag constructors, writing to $path")

      val outFile = new File(path)
      outFile.getParentFile.mkdirs()

      val tagsFile = new File(tags)
      val tagList = Files.readAllLines(tagsFile.toPath).asScala

      val tagBuilders = tagList.map(tag => {
        val defName = if (tag == "object") "`object`" else tag
        s"""  def $defName(attrs: Map[String, js.Any], children: js.Any*): ViewNode =
           |    mkTag("$tag", attrs, children:_*)
         """.stripMargin
      }).mkString("\n")

      val fileContents =
        s"""package cf.srxl.hyperapp
           |
           |import scala.scalajs.js
           |import scala.scalajs.js.JSConverters._
           |
           |object Tags {
           |  private def mkTag(name: String, attrs: Map[String, js.Any], children: js.Any*): ViewNode =
           |    Hyperapp.h(name, attrs.toJSDictionary, children:_*)
           |
           |$tagBuilders
           |}
        """.stripMargin.getBytes

      Files.write(outFile.toPath, fileContents)
  }
}
