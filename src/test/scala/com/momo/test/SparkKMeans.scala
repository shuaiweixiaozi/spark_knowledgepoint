package com.momo.test

import breeze.linalg.{DenseVector, Vector, squaredDistance}
import org.apache.spark.sql.SparkSession

/**
  * Created by hzhb on 2017/8/14.
  */
object SparkKMeans {

  def parseVector(line:String):Vector[Double] = {
    DenseVector(line.split(' ').map(_.toDouble))
  }

  def closesPoint(p:Vector[Double],centers:Array[Vector[Double]]):Int = {
    var bestIndex = 0
    var closest = Double.PositiveInfinity

    for(i <- 0 until centers.length){
      val tempDist = squaredDistance(p,centers(i))
      if (tempDist < closest){
        closest = tempDist
        bestIndex = i
      }
    }
    bestIndex
  }

  def showWarning(){
    System.err.println(
      """
        WARN:This is a naive implementation of KMeans Clustering and is given as an example!
        |Please use org.apache.spark.ml.clustering.KMeans
        |for more conventional use
      """.stripMargin)
  }

  def main(args: Array[String]) {
    showWarning()

    val spark = SparkSession
      .builder()
      .appName("SparkMeans")
      .getOrCreate()

    val lines = spark.read.textFile(args(0)).rdd
    val data = lines.map(parseVector _).cache()
    val k = args(1).toInt
    val convergeDist = args(2).toDouble

    val kPoints = data.takeSample(withReplacement=false,k,42)
    var tempDist = 1.0

    while(tempDist > convergeDist){
        val closest = data.map(p => (closesPoint(p,kPoints),(p,1)))
        val pointStats = closest.reduceByKey{case ((p1,c1),(p2,c2)) => (p1+p2,c1+c2)}

        val newPoints = pointStats.map{pair =>
            (pair._1,pair._2._1*(1.0 / pair._2._2))
          }.collectAsMap()

        tempDist = 0.0
        for(i <- 0 until k){
          tempDist += squaredDistance(kPoints(i),newPoints(i))
        }

        for(newP <- newPoints){
          kPoints(newP._1) = newP._2
        }
        println("Finished iteration (delta = " + tempDist + ")")
    }
    println("Final centers:")
    kPoints.foreach(println)
    spark.stop()
  }
}
