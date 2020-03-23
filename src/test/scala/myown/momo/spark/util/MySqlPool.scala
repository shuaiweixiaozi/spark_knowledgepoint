package myown.momo.spark.util

import java.sql.{Connection, DriverManager}
import java.util

/**
  * Created by hzhb on 2017/9/22.
  */
object MySqlPool {

  private val max = 8              // 连接池连接总数
  private val connectionNum = 10   // 每次产生连接数
  private var conNum = 0           // 当前连接池已产生的连接数
  private val pool = new util.LinkedList[Connection]()   // 连接池

  // 获取连接
  def getJdbcConn():Connection = {
    // 同步代码块
    AnyRef.synchronized({
      if(pool.isEmpty){
        // 加载驱动
        preGetConn()
        for(i <- 1 to connectionNum){
          val conn = DriverManager.getConnection("jdbc:mysql://192.168.1.99:3306/db_sparkstreaming_result","su","123456")
          pool.push(conn)
          conNum += 1
        }
      }
      pool.poll()
    })
  }

  private def preGetConn(): Unit ={
    // 控制加载
    if(conNum < max && !pool.isEmpty){
      println("jdbc Pool has no connection now,please wait a moments")
      Thread.sleep(2000)
      preGetConn()
    }else{
      Class.forName("com.mysql.jdbc.Driver")
    }
  }

  // 释放连接
  def releaseConn(conn:Connection): Unit ={
    pool.push(conn)
  }
}

object TestConnectionPoll{

  def main(args: Array[String]): Unit = {
    for(x <- 1 to 20){
      val con = MySqlPool.getJdbcConn()
      println("current connect:" + x + con)
      if(x == 6){
        println("release conn:" + x + con)
        MySqlPool.releaseConn(con)
      }
    }
  }
}
