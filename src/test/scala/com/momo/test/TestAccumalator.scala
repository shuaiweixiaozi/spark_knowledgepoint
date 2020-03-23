package com.momo.test

import org.apache.spark._
import scala.util.Random
/**
  * Created by hzhb on 2017/8/11.
  */
object TestAccumalator {

  def main(args: Array[String]): Unit = {
    val sconf = new SparkConf().setAppName("TestAccumalator").setMaster("local[1]")
    val sc = new SparkContext(sconf)

    val totalAccumulator = sc.accumulator(0L,"total_counter")
    val accumulator0 = sc.accumulator(0L,"counter0")
    val accumulator1 = sc.accumulator(0L,"count1")

    val count = sc.parallelize(1 to 10000,5).map{ i=>
      totalAccumulator += 1
      val x = (new scala.util.Random).nextInt() * 2 -1
      val y = (new scala.util.Random).nextInt() * 2 -1
      if(x*x + y*y <1){
        accumulator0 += 1
      }else{
        accumulator1 += 1
      }
      if(x*x + y*y<1) 1 else 0
    }.reduce(_+_)
  }
}
