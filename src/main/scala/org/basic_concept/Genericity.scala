package org.basic_concept

class Genericity {

}

//泛型类
class B[T, S](val v1:T, val v2:S)

//泛型上界(upper bound) <: 或 <%(view bound包含类型隐式转换)
class Pair[T <: Comparable[T]](v1:T, v2:T){
  def smaller():T = {
    if(v1.compareTo(v2) < 0)
      v1
    else
      v2
  }
}

object Basic9 extends App{

//  //泛型方法
//  def print[A](content :A): Unit ={
//    println(content)
//  }
//
//  print[String]("XXX")

  var p = new Pair[String]("A","B")
  println(p.smaller())

}
