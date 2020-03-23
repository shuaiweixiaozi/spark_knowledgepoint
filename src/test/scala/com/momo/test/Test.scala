package com.momo.test
import org.apache.spark.mllib.linalg.Matrices
import org.apache.spark.mllib.linalg.Matrix
/**
  * Created by hzhb on 2017/8/7.
  */
object Test {

  val sm:Matrix = Matrices.sparse(3,3,Array(0,2,3,6),Array(0,2,1,0,1,2),Array(1,2,3,4,5,6))

  def main(args: Array[String]): Unit = {
//    val p = new Person("MOMO",11,"male")
//    println(p.name + ":" + p.gender)
//    val stu = new Student("ZXW",18,"engineer")
//    println(stu.toString)
//    val s1 = new Student1
//    s1.speak
//    println(s1.name + ":" + s1.age)
//    val tl = new TestLogger
//    tl.abstractLog("xxx")
    val acc = new MyAccount with MessageLogger
    acc.save()
  }
}

// 1、主构造器直接跟在类名后面，主构造器中的参数，最后会被编译成字段
// 2、主构造器执行的时候，会执行类中的所有语句
// 3、假设参数申明时候不带val或var，那么相当于private[this]
class Person(var name:String,val age:Int){
  println("this is the primary constructor!")

  var gender : String = _
  val school:String = ""
  //1、附属构造器名称为this
  //2、每一个附属构造器必须首先调用已经存在的构造器
  def this(name : String,age : Int,gender : String){
    this(name,age)
    this.gender = gender
  }
}

class Student(name:String,age:Int,val major:String) extends Person(name,age){
  println("this is the subclass of Person,major is " + major)
  override val school:String = "BJU"
  override def toString: String = "Override toString..."
}

abstract class Person1{
  def speak
  val name:String
  var age:Int
}

class Student1 extends Person1{
  override def speak = {
    println("speak ....")
  }
  val name = "aaa"
  var age = 18
}

trait ConsoleLogger{

  def log(msg:String): Unit ={
  println("save money : " + msg)
  }
}

trait MessageLogger extends ConsoleLogger{
  override def log(msg: String): Unit ={
    println("save money to bank : " + msg)
  }
}

abstract class Account{
  def save
}

class MyAccount extends Account with ConsoleLogger{
    def save(): Unit = {
    log("100")
  }
}

//class TestLogger extends Logger{
//  def abstractLog(msg:String) {
//    log("xxx")
//  }
//}