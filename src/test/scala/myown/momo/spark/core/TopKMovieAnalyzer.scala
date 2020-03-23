package myown.momo.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hzhb on 2017/8/21.
  */
object TopKMovieAnalyzer {

  def main(args: Array[String]): Unit = {
    var dataPath = "D:\\scalaworkspace\\sparktraining-master\\data\\ml-1m"
    val conf = new SparkConf().setAppName("TopKMovieAnalyzer")
    if(args.length > 0){
      dataPath = args(0)
    }else{
      conf.setMaster("local[3]")
    }
    val sc = new SparkContext(conf)

    /*
    step 1: Create RDDs
     */
    val DATA_PATH = dataPath
    val ratingsRdd = sc.textFile(DATA_PATH + "/ratings.dat")

    /*
    step 2: Extract columns from RDDs
     */
    //users:RDD[(userID,movieID,score)]
    val ratings = ratingsRdd.map(_.split("::")).map(x => (x(0),x(1),x(2))).cache()

    /*
    step 3: analyze result
     */
    val topKScoreMostMovie = ratings.map(x => (x._2,(x._3.toInt,1)))
          .reduceByKey{(v1,v2) => (v1._1 + v2._1,v1._2 + v2._2)}
          .map{x => (x._2._1.toFloat/x._2._2.toFloat,x._1)}
          .sortByKey(false).take(10)

    topKScoreMostMovie.foreach(println)

    val topKmostPerson = ratings.map(x => (x._1,1)).reduceByKey(_ + _)
          .map(x => (x._2,x._1))
          .sortByKey(false)
          .take(10)
          .foreach(println)
    sc.stop()
  }
}
