package com.momo.test

/**
  * Created by hzhb on 2017/8/8.
  */
class Basic5 {

}

object Basic5 extends App {
  val value = 1
  val result = value match  {
      case 1 => "one"
      case 2 => "two"
      case _ => "some other number....."
  }
  val result2 = value match {
      case i if i == 1 => "one"
      case i if i == 2 => "two"
      case _ => "some other number..."
  }
  def t(obj : Any) = obj match {
      case x : Int => println("Int")
      case s : String => println("String")
      case _ => println("unknown type")
  }

  println("result of match is " + result)
  println("result2 of match is " + result2)
  t(1L)
}