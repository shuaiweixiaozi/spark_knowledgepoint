package com.momo.test

import breeze.linalg.{DenseVector, Vector}
import java.util.{Random, StringTokenizer}
import scala.math.exp

import org.apache.spark.sql.SparkSession

/**
  * Created by hzhb on 2017/8/14.
  */
object SparkHdfsLR {

  val D = 10
  val rand = new Random(42)

  case class DataPoint(x:Vector[Double],y:Double)

  def parsePoint(line:String):DataPoint={
    val tok = new StringTokenizer(line," ")
    val y = tok.nextToken.toDouble
    val x = new Array[Double](D)
    var i = 0
    while (i < D){
      x(i) = tok.nextToken.toDouble
      i += 1
    }
    DataPoint(new DenseVector[Double](x),y)
  }

  def showWarning(): Unit ={
    System.err.println(
      """WARN: This is a naive implementation of Logistic Regression and is given as an example!
         |Please use org.apche.apark.ml.classificatin.LogisticRegression
         |for more conventional use.
      """.stripMargin
    )
  }

  def main(args: Array[String]): Unit = {
    if(args.length<2) {
      System.err.print("Usage:SparkHdfsLR <file> <iters>")
      System.exit(1)
    }

    showWarning()

    val spark = SparkSession
          .builder
          .appName("SparkHdfsLR")
          .getOrCreate()

    val inputPath = args(0)
    val lines = spark.read.textFile(inputPath).rdd

    val points = lines.map(parsePoint).cache()
    val ITERATIONS = args(1).toInt

    //Initialize w to a random value
    val w = DenseVector.fill(D){2*rand.nextDouble() - 1}
    println(("Initial w:" + w))

    for (i <- 1 to ITERATIONS){
      println("On iteration " + i)
      val gradient = points.map{p => p.x * (1 / (1 + exp(-p.y * (w.dot(p.x))))-1) * p.y}
            .reduce(_+_)
      w -= gradient
    }
    println("Final w: " + w)
    spark.stop()
  }
}
