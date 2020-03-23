package myown.momo.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hzhb on 2017/8/21.
  */
object MovieUserAnalyzer {

  def main(args: Array[String]): Unit = {
    var dataPath = "D:\\scalaworkspace\\sparktraining-master\\data\\ml-1m"
    var conf = new SparkConf().setAppName("PopularMovieAnalyzer")
    if(args.length > 0){
      dataPath = args(0)
    }else{
      conf.setMaster("local[3]")
    }
    val sc = new SparkContext(conf)

    /*
    step 1:Create RDDs
     */
    val DATA_PATH = dataPath
    val MOVIE_TITAL = "Lord if the Rings, The (1978)"
    val MOVIE_ID = "2016"

    val usersRdd = sc.textFile(DATA_PATH + "/users.dat")
    val ratingsRdd = sc.textFile(DATA_PATH + "/ratings.dat")

    /*
    step 2:Extract columns from RDDS
     */
    //users:RDD[(userID,(gender,age))]
    val users = usersRdd.map(_.split("::")).map{x => (x(0),(x(1),x(2)))}

    //rating:RDD[Array(userID,movieID,ratings,timestamp)]
    val rating = ratingsRdd.map(_.split("::"))

    //usremovie RDD[(userID,movieID)]
    val usermovie = rating.map(x => (x(0),x(1))).filter(_._2.equals(MOVIE_ID))

    /*
      step 3: join RDDs
      */
    //userRating:RDD[(userID,(movieID,(gender,age)))]
    val userRating = usermovie.join(users)

    //userRating.take(1).foreach(print)

    //movieuser:RDD[(movieID,(movieTile,(gender,age)))]
    val userDistribution = userRating.map{ x=> (x._2._2,1)}.reduceByKey(_ + _)
    userDistribution.collect().foreach(println)

    sc.stop()
  }
}
