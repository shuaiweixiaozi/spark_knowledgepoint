package org.basic_concept

class ApplyAndSingleton {

}

class ApplyTest{
  def apply(): String = "APPLY"
  def test():Unit ={
    println("ApplyTest test method")
  }
}

object ApplyTest1{
  var count: Int = 0
  def apply(): ApplyTest = new ApplyTest()
  def incr(): Unit = {
    count = count + 1
  }
}

object Basic4 extends App{
//  val a= ApplyTest1()
//  a.test
//  val a = new ApplyTest
//  println(a())

  for (i <- 1 to 10){
    ApplyTest1.incr()
  }
  println(ApplyTest1.count)
}