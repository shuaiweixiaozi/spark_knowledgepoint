package org.basic_concept;

// 抽象类知识点
// 1、类的一个或多个方法没有完整的定义
// 2、申明抽象方法不需要加abstract关键字，只需要不写方法体
// 3、子类重写父类的抽象方法时不需要加override
// 4、父类可以申明抽象字段(没有初始值的字段)
// 5、子类重写父类的抽象字段时不需要加override
class AbstrackClassKnowledgePoint {
}

abstract class Person1{
  val name: String
  val age: Int
  def speak
}

class Student1 extends Person1{
  override val name = "AAA"
  override val age: Int = 10

  override def speak: Unit = {
    println("Student speak:" + this.name + ":" + this.age)
  }
}

//trait Logger{
//  def log(msg: String): Unit ={
//    println("info:" + msg)
//  }
//}
//
//class Test extends Logger{
//  def test: Unit ={
//    log("xxx")
//  }
//}

//trait Logger{
//  def log(msg: String)
//}
//
//trait ConsoleLogger extends Logger{
//  override def log(msg: String): Unit = {
//    println("info:" + msg)
//  }
//}
//
//class Test extends ConsoleLogger {
//  def test: Unit ={
//    log("test")
//  }
//}

// trait: 特质。带有字段、行为的集合，混入类中，通过with关键字，一个类可以扩展多个特质
trait ConsoleLogger {
  def log(msg: String): Unit = {
    println("info:" + msg)
  }
}

trait MessageLogger extends ConsoleLogger{
  override def log(msg: String): Unit = {
    println("save money:" + msg)
  }
}

abstract class Account{
  def save
}


class MyAccount extends Account with ConsoleLogger{
  override def save: Unit = {
    log("100")
  }
}

object Basic3 extends App{
//  val s = new Student1
//  s.speak
//  val t = new Test
//  t.test
//  val a = new MyAccount
//  a.save
  // 带有特质的对象
  val acc = new MyAccount with MessageLogger
  acc.save

}