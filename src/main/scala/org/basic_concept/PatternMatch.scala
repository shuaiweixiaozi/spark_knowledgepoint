package org.basic_concept

class PatternMatch {

}


abstract class Person3

case class Teacher(name: String) extends Person3
case class Student(name: String) extends Person3



object Basic5 extends App{

//  def m(p: Person3): Unit= {
//    p match {
//      case Teacher(_) => print("This is teacher")
//      case Student(_) => print("This is student")
//      case _ => print("Other Type")
//    }
//  }
//
//  m(Teacher("A"))
//  val value = 1
//  val result = value match {
//    case 1 => "one"
//    case 2 => "two"
//    case _ => "some other number"
//  }
//
//  val result2 = value match {
//    case i if i == 1 => "one"
//    case i if i == 2 => "two"
//    case _ => "some other number"
//  }
//  println("return of match is :" + result)
//  println("return of match is :" + result2)
//
//  def t(obj: Any) = obj match {
//    case x: Int => println("Int")
//    case s: String => println("String")
//    case _ => "unknow type"
//  }
//
//  t("a")
  val m = Map(1->2)
  m.get(10) match {
    case Some(x) => print(x)
    case None => print("Key not Found")
  }
}
