package org.basic_concept

import java.io.File

import scala.collection.mutable.ArrayBuffer

object BasicConceptKnowledgePoint {

  // 值(val): 赋值后不可变  val 值名称:类型 = xxx
  // 变量(var): 赋值后可以改变 var 值名称:类型 = xxx
  // 一般不需要显式指定类型，可以从赋值中推断出来
  def testValVar(): Unit = {
    val x: Int = 1
    var y = 2
    y = 3
  }

  // 函数的定义
  // def 方法名(参数名: 参数类型): 返回类型 = {
  //    block内最后一行为返回值
  // }
  // 当返回值为Unit时可以定义为
  // def 方法名(参数名： 参数类型){
  //     block内容
  // }
  // 没有参数的方法可以不带圆括号访问
  def gcd(x:Long, y:Long) :Long = if(y==0) x else gcd(y, x % y)

  def testArray(): Unit = {
    //定义定长数组
    val array: Array[String] = new Array[String](_length = 2)
    //定义并初始化array
    val array1: Array[String] = Array("1","2","3")
    println(array1(1))

    // 变长数组
    val buffArray: ArrayBuffer[Int] = ArrayBuffer[Int]()
    buffArray += (3, 4, 5)
    println(buffArray)
  }


  def testMap(): Unit = {
    //不可变Map
    val age: Map[String, Int] = Map("jack" -> 20, "Lucy" -> 18)
    //可变Map
    val mutMap = scala.collection.mutable.Map[String, Int]("jack"->10,"luch"->9)
    println(mutMap("jack"))
    //遍历Map
    for ((k, v) <- mutMap){
      println(k,v)
    }

  }


  def printFiles(): Unit ={
    // <- 生成器，在执行过程中，集合filesHere中(Array[File])的元素将依次赋给file
    // file类型为File，打印时调用其toString方法将文件名称打印出来
    val filesHere = (new File(".")).listFiles()
    for (file <- filesHere){
      println(file)
    }
  }

  //测试生成器
  def testGenerate=
    // 带有多条件的for循环语句
//    for (i <- 1 to 5 if i % 2 == 0; if i % 4 == 0) yield i
    for (i <- 1 to 5 if i % 2 == 0) yield i

  def main(args: Array[String]): Unit = {
//    print(gcdLoop(3, 1))
//    print(gcd(3, 1))
//    printFiles()
//    println(testGenerate)
    testMap()
  }
}
