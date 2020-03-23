package org.basic_concept

class ScalaCollection {

}


object Basic8 extends App{
  // 不可变List
  val l1 = scala.collection.immutable.List(1, 2, 3)
//  println("head: " + l1.head)
  // 可变List
  val l2 = scala.collection.mutable.ListBuffer(11, 12, 13)
  l2 += 111
  l2 -= 12
//  println(l2)

  //不可变Set
  val s1 = Set(1, 2, 3)
  print(s1 + 4)
  print(s1 ++ List(4, 5, 6))
  // 可变Set
  val s2 = scala.collection.mutable.Set(11, 12, 13)

  val result = l2 match {
    case i if i(0) == 11 => println("case 1")
    case i if i.length ==3 && i(1) == 2 => println("case 2")
    case _ => println("No match")
  }

  def _find(list: List[Int])(func: List[Int] => List[Int]) = func(list)

  def find =  _find(List(1,2,3))_

  println(find(xs => xs.filter(_ >2)))
  println(find(xs => xs.map(_ * 2)))

}