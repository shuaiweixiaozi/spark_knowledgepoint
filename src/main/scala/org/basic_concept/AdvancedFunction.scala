package org.basic_concept

class AdvancedFunction {

}

object Basic7 extends App{
  // 匿名函数: （参数名 : 参数类型） => 表达式
  // 可以将匿名函数赋值给val、var
  val nimingFunc = (x: Int) => x+2
  println(nimingFunc(1))

  // 函数作为一个参数 def fun( f : (Int,Int) => Int ) : Int
  def fun(f: (Int, Int) => Int) = f(2, 3)
  println(fun((x:Int, y:Int) => x + y))

  // .....
  def mulBy(factor: Double) = (x: Double) => x * factor
  val mulBy3 = mulBy(3)
  val mulBy5 = mulBy(5)
  println(mulBy3(6))
  println(mulBy5(6))

  // 参数简化与类型推导
  val array1: Array[Int] = Array(1, 2, 3)
  array1.map((x: Int) => println(x))
  array1.map((x) => println(x))
  array1.map(x => println(x))
  array1.map(println(_))

  // partial function(部分函数)
  def add(x : Int, y : Int, z : Int) = x + y + z
  def _add = add(_:Int, _:Int, 0)
  println(_add(1,2))

  //currying 柯里化
  def mul(x: Int)(y:Int) = x * y
  def mul2 = mul(2)_
  println(mul2(3))

}