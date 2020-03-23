package org.training.spark.mapwithstatetest

import com.alibaba.fastjson.JSON
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.training.spark.util.KafkaRedisProperties

object UserClickCountAnalyticsWithMapwithstateTest {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("UserClickCountAnalytics")
    if (args.length == 0) {
      conf.setMaster("local[2]")
    }

    val ssc = new StreamingContext(conf, Seconds(15))
    ssc.checkpoint("d:\\mapwithstateCheckpoint")

    // Kafka configurations
    val topics = KafkaRedisProperties.KAFKA_RECO_TOPIC.split("\\,").toSet
    println(s"Topics: ${topics}.")

    val brokers = KafkaRedisProperties.KAFKA_ADDR
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder"
    )


    // Create a direct stream
    val kafkaStream = KafkaUtils
        .createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    val initialRDD = ssc.sparkContext.parallelize(List[(String, Int)]())

    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    val word = kafkaStream.map(line => {
          System.out.println("stock:"+line._2.split(",")(2).toString+" ;count:"+line._2.split(",")(4).toInt)
      (line._2.split(",")(2).toString,line._2.split(",")(4).toInt)})
//    val word = messages.flatMap(_._2.split(" ")).map { x => (x, 1) }
    //自定义mappingFunction，累加单词出现的次数并更新状态
    val mappingFunc = (word: String, count: Option[Int], state: State[Int]) => {
      val sum = count.getOrElse(0) + state.getOption.getOrElse(0)
      val output = (word, sum)
      state.update(sum)
      output
    }
    //调用mapWithState进行管理流数据的状态
    val stateDstream = word.mapWithState(StateSpec.function(mappingFunc).initialState(initialRDD)).print()
    ssc.start()
    ssc.awaitTermination()


//    val conf = new SparkConf().setAppName("UserClickCountAnalytics")
//    if (args.length == 0) {
//      conf.setMaster("local[2]")
//    }
//
//    val ssc = new StreamingContext(conf, Seconds(5))
//
//    // Kafka configurations
//    val topics = KafkaRedisProperties.KAFKA_RECO_TOPIC.split("\\,").toSet
//    println(s"Topics: ${topics}.")
//
//    val brokers = KafkaRedisProperties.KAFKA_ADDR
//    val kafkaParams = Map[String, String](
//      "metadata.broker.list" -> brokers,
//      "serializer.class" -> "kafka.serializer.StringEncoder"
//    )
//
//
//    // Create a direct stream
//    val kafkaStream = KafkaUtils
//        .createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
//
//    val initialRDD = ssc.sparkContext.parallelize(List[(String, Float)]())
//
//    val events = kafkaStream.map(line => (line._2.split(",").indexOf(2),line._2.split(",").indexOf(4).toFloat))
//
//    //自定义mappingFunction，累加单词出现的次数并更新状态
//    val mappingFunc = (stock: String, count: Option[Float], state: State[Float]) => {
//      val sum = count.getOrElse(0) + state.getOption.getOrElse(0)
//      val output = (stock, sum)
//      state.update(sum)
//      output
//    }
//
//    //调用mapWithState进行管理流数据的状态
//    val stateDstream = events.mapWithState(StateSpec.function(mappingFunc).initialState(initialRDD)).print()
//
////    // Compute user click times
////    val userClicks = events.map(x => (x.getString("uid"), x.getLong("click_count"))).reduceByKey(_ + _)
////    userClicks.foreachRDD(rdd => {
////      rdd.foreachPartition(partitionOfRecords => {
//////        val jedis = RedisClient.pool.getResource
////        partitionOfRecords.foreach(pair => {
////          try {
////            val uid = pair._1
////            val clickCount = pair._2
//////            jedis.hincrBy(clickHashKey, uid, clickCount)
////            println(s"Update uid ${uid} to ${clickCount}.")
////          } catch {
////            case e: Exception => println("error:" + e)
////          }
////        })
////        // destroy jedis object, please notice pool.returnResource is deprecated
//////        jedis.close()
////      })
////    })
//
//    ssc.start()
//    ssc.awaitTermination()
  }
}