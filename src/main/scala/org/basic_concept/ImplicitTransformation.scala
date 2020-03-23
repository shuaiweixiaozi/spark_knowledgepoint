package org.basic_concept

import java.io.File

import scala.io.Source

class ImplicitTransformation {
}

class RichFile(file: File){

  def read = Source.fromFile(file.getPath).mkString
}


class Pair2[T: Ordering](val first: T, val second: T){
  def smaller(implicit ord: Ordering[T]): T ={
    if (ord.compare(first, second) < 0) first else second
  }
}

class Line(len: Int){
  override def toString: String = {
    "Length of line: " + len
  }
}

object Context{
  // 隐式函数转换
  implicit def file2RichFile(file: File) = new RichFile(file)
  // 隐式参数转换
  implicit val ccc: String = "Hello"
}

object Basic10 extends App{
//  import Context._
//  val content = new File("./pom.xml").read
//  println(content)
//
//  def print(content: String)(implicit prefix: String): Unit ={
//    println(prefix + ":" + content)
//  }
//
//  print("Jack")

//  val p1 = new Pair2[Int](1, 2)
//  val p2 = new Pair2[String]("A", "B")
//  println(p1.smaller)
//  println(p2.smaller)

//  val l1 = new Line(1)
//  val l2 = new Line(2)
//  val p3 = new Pair2[Line](l1, l2)
//  println(p3.smaller)

}