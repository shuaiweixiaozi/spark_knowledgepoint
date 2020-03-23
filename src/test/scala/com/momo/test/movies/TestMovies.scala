package com.momo.test.movies

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
  * Created by hzhb on 2017/8/14.
  */
object TestMovies {

//  val spark = SparkSession.builder().appName("TestMovies").getOrCreate()

  val sparkConf = new SparkConf().setAppName("TestMovies").setMaster("Local[3]")
  val spark = new SparkContext(sparkConf)

  val usersRdd = spark.textFile("users.dat")
  val ratingsRdd = spark.textFile("ratings.dat")

  //users:RDD[(userID,(gender,age))]
  val users = usersRdd.map(_.split("::")).map{x=>(x(0),(x(1),x(2)))}

  //rating:RDD[Array(userID,movieID,ratings,timestamp)]
  val rating = ratingsRdd.map(_.split("::"))

  //usermovie:RDD[(userID,movieID)]
  val movie_id = ""   //指定的movieID
  val usermovie = rating.map{x=>(x(0),x(1))}.filter(_._2.equals(movie_id))

  //Join rdd
  //userRating:RDD[(userID,(movieID,(gender,age)))]
  val userRating = usermovie.join(users)

  //movieuser:RDD[(movieID,(movieTile,(gender,age)))]
  val userDistribution = userRating.map{x => (x._2._2,1)}.reduceByKey(_+_)

  userDistribution.collect().foreach(println)
}
