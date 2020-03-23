package org.basic_concept

/**
  * Created by momo 2019.08.09
  */
class ClassKnowledgePoint {

}

// 1、主构造器直接跟在类名后面，主构造器中的参数，最后会被编译成字段
// 2、主构造器执行的时候，会执行类中的所有语句
// 3、假设参数申明不带val和var，那么相当于private[this]
class Person(val name: String, val age:Int){
  println("Init Person")
  var gender: String = _
  val school = "ZJU"

  // 1、附属构造器名称为this
  // 2、每一个附属构造器必须首先调用已经存在的主构造器或者附属构造器
  def this(name: String, age: Int, gender: String){
    this(name, age)
    this.gender = gender
  }
//  var name: String = _ // 会生成getter、setter方法
//  val age = 10  // 只会生成
//  private[this] val gender = "male"
}

class Student(name: String, age: Int, val major: String) extends Person(name, age){
  println("Init Student")
  // 重写字段
  override val school: String = "BJU"
  // 重写方法
  override def toString: String = super.toString
}

object Basic2{
  def main(args: Array[String]): Unit = {
//    val p = new Person()
//    p.name = "jack"
//    println(p.name)
//    println(p.name + ":" + p.gender)

//    val p = new Person("jack", 10, gender = "male")
//    println(p.name, p.age, p.gender)

    val s = new Student("Justin", 30, "Math")
  }
}