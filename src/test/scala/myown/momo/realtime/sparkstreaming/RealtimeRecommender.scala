package myown.momo.realtime.sparkstreaming

import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.training.spark.proto.Spark.NewClickEvent
import org.training.spark.util.{KafkaRedisProperties, RedisClient}

/**
  * Created by hzhb on 2017/8/24.
  */
object RealtimeRecommender {

  def main(args: Array[String]): Unit = {
    val Array(brokers,topics) = Array(KafkaRedisProperties.KAFKA_ADDR,KafkaRedisProperties.KAFKA_RECO_TOPIC)

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("RealtimeRecommender").setMaster("local[3]")
    val ssc = new StreamingContext(sparkConf,Seconds(2))


    //create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String,String]("metadata.broker.list" -> brokers,"auto.offset.reset" -> "smallest")
    val messages = KafkaUtils.createDirectStream[String,Array[Byte],StringDecoder,DefaultDecoder](ssc,kafkaParams,topicsSet)

    messages.map(_._2).map{event => NewClickEvent.parseFrom(event)}
        .foreachRDD{
          rdd => rdd.foreachPartition{ partition =>
              val jedis = RedisClient.pool.getResource
              partition.foreach{ event =>
                println("NewClickEvent:" + event)
                val userId = event.getUserId
                val itemID = event.getItemId
                val key = "II:" + itemID
                val value = jedis.get(key)
                if (value != null){
                  jedis.set("RUI:" + userId,value)
                  println("Finish recommendation to user:" + userId)
                }
              }
            jedis.close()
          }
        }
    ssc.start()
    ssc.awaitTermination()
  }
}
