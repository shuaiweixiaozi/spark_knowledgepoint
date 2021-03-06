package myown.momo.realtime.sparkstreaming

import com.alibaba.fastjson.JSON
import kafka.serializer.StringDecoder
import myown.momo.spark.util.KafkaMysqlPropertis
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.training.spark.util.{KafkaRedisProperties, RedisClient}

object UserClickCountAnalytics {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UserClickCountAnalytics")
    if (args.length == 0) {
      conf.setMaster("local[1]")
    }

    val ssc = new StreamingContext(conf, Seconds(5))

    // Kafka configurations
    val topics = KafkaMysqlPropertis.KAFKA_USERCLICK_TOPIC.split("\\,").toSet
    println(s"Topics: ${topics}.")

    val brokers = KafkaMysqlPropertis.KAFKA_BROKER_SERVER
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder"
    )

//    val clickHashKey = "app::users::click"

    // Create a direct stream
    val kafkaStream = KafkaUtils
        .createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    val events = kafkaStream.flatMap(line => {
      println(s"Line ${line}.")
      val data = JSON.parseObject(line._2)
      Some(data)
    })

    // Compute user click times
    val userClicks = events.map(x => (x.getString("uid"), x.getLong("click_count"))).reduceByKey(_ + _)
    userClicks.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {
//        val jedis = RedisClient.pool.getResource
        partitionOfRecords.foreach(pair => {
          try {
            val uid = pair._1
            val clickCount = pair._2
//            jedis.hincrBy(clickHashKey, uid, clickCount)
            println(s"Update uid ${uid} to ${clickCount}.")
          } catch {
            case e: Exception => println("error:" + e)
          }
        })
        // destroy jedis object, please notice pool.returnResource is deprecated
//        jedis.close()
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}