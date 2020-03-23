package com.momo.test

/**
  * Created by hzhb on 2017/8/9.
  */
object Bounds extends App{
//  println(new Pair[Int](1,2).smaller)
//  new X(1).x("class X method x")

  implicit object Line extends LineOrdering

//  val p1 = new Pair(1,2)
//  val p2 = new Pair("A","B")
//
//  println(p1.smaller)
//  println(p2.smaller)

  val l1 = new Line(1)
  val l2 = new Line(2)

  val p3 = new Pair(l1,l2)
  println(p3.smaller)
}

//view Bounds： T <% V T到V的隐士转换
//class Pair[T <% Comparable[T]](val first:T,val second:T){
//
//  def smaller = if(first.compareTo(second)<0) first else second
//
//}
// context Bounds: [T:M]-------->M[T]
// 类型通配符  scala:[_ <: AAA]  Java:<? extends AAA>
class X[-T](val y:Int){
  def x(t:T): Unit ={
    println(t)
  }
}

class Pair[T:Ordering](val first:T,val second:T){
  def smaller(implicit ord:Ordering[T])={
    if(ord.compare(first,second)<0) first else second
  }
}

class Line(val len:Double){
  override def toString() = "Length of line : " + len
}

trait LineOrdering extends Ordering[Line]{
  override def compare(x:Line,y:Line)={
    if(x.len<y.len) -1
    else if(x.len==y.len) 0
    else 1
  }
}