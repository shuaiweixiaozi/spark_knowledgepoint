package com.momo.test

import scala.io.Source
/**
  * Created by hzhb on 2017/8/8.
  */
object CaseClassDemo extends App{

  val m = Map(1->2)
  m.get(10) match {
      case Some(v) => println(v)
      case None => println("No such key")
  }
//  m.getOrElse(10,100)

//  val filePath:String = ""
//  val file = Source.fromFile(filePath)
//  for(line <- file.getLines()){
//    println(line)
//  }

//  def m(p:People): Unit ={
//    p match {
//        case i:Teacher => println("I am a teacher")
//        case i:Policy=> println("I am a policy")
//        case _ => println("I am not a People")
//    }
//  }
//  m(Teacher("Lucy"))
}

abstract class People

case class Teacher(name:String) extends People
case class Policy(name:String) extends People