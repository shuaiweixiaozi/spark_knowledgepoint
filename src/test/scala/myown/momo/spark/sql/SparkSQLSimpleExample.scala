package myown.momo.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Created by hzhb on 2017/8/30.
  */
object SparkSQLSimpleExample {

  case class User(userID:String,gender:String,age:String,occupation:String,zipcode:String)

  def main(args: Array[String]): Unit = {
    var dataPath = "D:\\scalaworkspace\\sparktraining-master\\data"
    val conf = new SparkConf()
    if(args.length > 0){
      dataPath = args(0)
    }else{
      conf.setMaster("local[3]")
    }

    val spark = SparkSession.builder().appName("SparkSQLSimpleExample").config(conf).getOrCreate()

    val sc = spark.sparkContext
    /*
    create RDDs
     */
    val DATA_PATH = dataPath
    val usersRdd = sc.textFile(DATA_PATH + "\\ml-1m\\users.dat")

    /*
    Method 1:通过显示为RDD注入schema。将其变换为DataFrame
     */

    import spark.implicits._

    val usrRDD = usersRdd.map(_.split("::")).map(p => User(p(0),p(1),p(2),p(3),p(4)))
    val userDataFrame = usersRdd.toDS()
    println(userDataFrame.take(10))
//    println(userDataFrame.count())

    /*
    Method2 :通过反射方式，为RDD注入schema，将其变换为DataFrame
     */
    val schemaString = "userID gender age occupation zipcode"
    val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName,StringType,true)))
    val userRDD2 = usersRdd.map(_.split("::")).map(p => Row(p(0),p(1).trim,p(2).trim,p(3).trim,p(4).trim))
    val userDataFrame2 = spark.createDataFrame(userRDD2,schema)
//    println(userDataFrame2.take(2).toString)
    println(userDataFrame2.count())
//    userDataFrame2.write.mode(SaveMode.Overwrite).json(DATA_PATH + "\\user.json")
//    userDataFrame2.write.parquet(DATA_PATH + "\\user.parquet")

    /*
    读取json格式数据1： read.format("json").load(...)
     */
    val userJsonDF = spark.read.format("json").load(DATA_PATH + "\\user.json")

    /*
    读取json格式数据2： read.json(...)
     */
    val userJsonDF2 = spark.read.json(DATA_PATH + "\\user.json")

    /*
    读取parquet格式数据1: read.format
     */
    val userParquetDF = spark.read.format("parquet").load(DATA_PATH + "\\user.parquet")

    /*
    读取parquet格式数据2: read.parquet(...)
     */
    val ratingsRdd = sc.textFile(DATA_PATH + "\\ml-1m\\ratings.dat")

    val ratingSchemaString = "userID movieID Rating Timestamp"
    val ratingSchema = StructType(ratingSchemaString.split(" ").map(fieldName => StructField(fieldName,StringType,true)))
    val ratingRDD = ratingsRdd.map(_.split("::")).map(p => Row(p(0),p(1).trim,p(2).trim,p(3).trim))
    val ratingDataFrame = spark.createDataFrame(ratingRDD,ratingSchema)

    val mergedDataFrame = ratingDataFrame.filter("movieID = 2116").join(userDataFrame2,"userID").select("gender","age").groupBy("gender","age").count()

//    val mergedDataFrame2 = ratingDataFrame.filter("movieID = 2116").join(userDataFrame2,userDataFrame("userID") === ratingDataFrame("userID"),"inner")
//      .select("gender","age").groupBy("gender","age").count()

    mergedDataFrame.collect().foreach(println(_))

    userDataFrame.createOrReplaceTempView("users")
    val groupedUsers = spark.sql("select gender,age,count(*) as n from users group by gender,age")
    groupedUsers.map{u => (u.getAs[String]("userID").toLong,u.getAs[String]("age").toInt + 1)}.take(10).foreach(println(_))

    sc.stop()
  }
}
