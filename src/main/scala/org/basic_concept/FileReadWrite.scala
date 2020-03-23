package org.basic_concept
// 引入Source可以方便的实现读写文件
import scala.io.Source

class FileReadWrite {

}

object Basic6 extends App{
  val file = Source.fromFile("D:\\scalaworkspace\\spark_knowledgepoint\\pom.xml")
  for (line <- file.getLines()){
    println(line)
  }
}
