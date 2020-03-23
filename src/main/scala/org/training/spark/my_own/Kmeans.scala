package org.training.spark.my_own

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeans

/**
  * Created by hzhb on 2017/8/4.
  */
object Kmeans {

  def main(args: Array[String]): Unit = {

    var dataPath = "D:\\scalaworkspace\\sparktraining-master\\data\\mllib\\kmeans_data.txt"
    val conf = new SparkConf().setAppName("Kmeans")
    if(args.length > 0) {
      dataPath = args(0)
    } else {
      conf.setMaster("local[1]")
    }
    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)
    val examples = sc.textFile(dataPath).map(line => Vectors.dense(line.split(' ').map(_.toDouble))).cache()
    val numExamples = examples.count()
    println(s"numExamples = $numExamples.")

    val k = 3
    val numIterations = 20
    val model = new KMeans()
        .setInitializationMode(KMeans.RANDOM)
        .setK(k)
        .setMaxIterations(numIterations)
        .run(examples)

    val cost = model.computeCost(examples)
    println(s"Total cost = $cost.")

    // 打印数据模型的中心点
    println(s"Cluster centers: ")
    for(c <- model.clusterCenters){
      println(" " + c.toString)
    }

    //使用模型测试单点数据
    println("Vectors [0.2,0.2,0.2] is belongs to clusters:" + model.predict(Vectors.dense("0.2 0.2 0.2".split(" ").map(_.toDouble))))
    println("Vectors [0.25,0.25,0.25] is belongs to clusters:" + model.predict(Vectors.dense("0.25 0.25 0.25".split(" ").map(_.toDouble))))
    println("Vectors [8,8,8] is belongs to clusters:" + model.predict(Vectors.dense("8 8 8".split(" ").map(_.toDouble))))

    //交叉评估1，只返回结果
    val result1 = model.predict(examples)
    result1.saveAsTextFile("D:\\scalaworkspace\\sparktraining-master\\data\\mllib\\result1.txt")

    //交叉评估2，返回数据集和结果
    val result2 = examples.map{line =>
                    val prediction = model.predict(line)
                    line + " " + prediction
    }.saveAsTextFile("D:\\scalaworkspace\\sparktraining-master\\data\\mllib\\return2.txt")

    sc.stop()
  }

}
