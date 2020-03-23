package com.momo.test

/**
  * Created by hzhb on 2017/8/8.
  */
class ApplyTest{
  def test: Unit ={
    println("test")
  }
}

object Apply{
  var count = 0
  def apply(): ApplyTest = new ApplyTest()
  def static: Unit ={
    println("I'm a static method")
  }
  def incr = {
    count = count + 1
  }
}

class Basic4 {}

object Basic4 extends App{
//  ApplyTest.static
  for(i <- 1 to 10){
    Apply.incr
  }
  println(Apply.count)
}