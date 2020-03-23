package com.momo.test

import java.io.File

import scala.io.Source

/**
  * Created by hzhb on 2017/8/9.
  */
object ImplicitDemo extends App{
//  import Context.file2RichFile
//  new File("xxx").read
    import Context._
//  AAA.print("Lily")
    println(1.add2(2))
}

object AAA{
  def print(content:String)(implicit prefix:String): Unit ={
    println(prefix+":"+content)
  }
}

class RichFile(val file:File){
  def read = Source.fromFile(file.getPath).mkString
}

object Context{
  implicit def file2RichFile(f:File) = new RichFile(f)
  implicit val ccc:String = "Hello"

  implicit class HH(x:Int){
    def add2(y:Int) = y+2
  }
}