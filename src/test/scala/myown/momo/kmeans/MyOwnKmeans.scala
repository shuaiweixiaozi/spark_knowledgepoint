package myown.momo.kmeans

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hzhb on 2017/8/23.
  */
object MyOwnKmeans {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MyOwnKmeans").setMaster("local[3]")
    val spark = new SparkContext(conf)

    val data = spark.textFile("")
    val parsedData = data.map(s => Vectors.dense(s.split(" ").map(_.toDouble))).cache()

    //设置K均值算法的参数k=2，最大迭代次数为20，求解聚类结果：
    val clusters = new KMeans().setK(2).setMaxIterations(20).run(parsedData)

  //输出聚类的中心
    (0 to 1).foreach{id => println("Center of cluster " + (id + 1) + "is " + clusters.clusterCenters(id))}

  //输出聚类结果的类内平方误差之和
    println("Within Set Sum of Squared Errors = " + clusters.computeCost(parsedData))

    //假设需要利用聚类结果预测坐标[0.5,0.9,0.8]
    val point = Vectors.dense(Array(0.5,0.9,0.8))
    println("Point " + point + " belongs to cluster " + (clusters.predict(point) + 1))
  }
}
