package myown.momo.realtime.sparkstreaming

import com.alibaba.fastjson.JSON
import kafka.serializer.StringDecoder
import myown.momo.spark.util.{KafkaMysqlPropertis, MySqlPool}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * Created by hzhb on 2017/9/20.
  */
object TraceLogAnalytics {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("TraceLogAnalytics")
    if (args.length == 0 ){
      conf.setMaster("local[2]")
    }

    val ssc = new StreamingContext(conf,Seconds(5))

    ssc.checkpoint("D:\\tracelog_checkpoint")

    // kafka configurations
    val topics = KafkaMysqlPropertis.KAFKA_TRACE_TOPIC.split("\\,").toSet
    println(topics)

    val brokers = KafkaMysqlPropertis.KAFKA_BROKER_SERVER
    val kafkaParams = Map[String,String](
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder"
      )

    // create a direct stream
    val kafkaStream = KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topics)

    val events = kafkaStream.flatMap(
      line => {
        println(s"Line ${line}.")
        val data = JSON.parseObject(line._2)
        Some(data)
      }
    )

    // compute trace log
    val traceLog = events.map(obj => (obj.getString("payload"))).map(_.split("\\|")).map(x => (x(1),1)).reduceByKey(_ + _)
//      .foreachRDD(
//        rdd => {
//          rdd.foreachPartition(partitionOfRecords => {
//              partitionOfRecords.foreach(pair => {
////                val userId = pair._1
////                val count = pair._2
////                println("userID="+userId+",count="+count)
//                println(pair)
//              })
//            }
//          )
//        }
//    )

    val addFunc = (currValues:Seq[Int],prevValueState:Option[Int]) => {
      var newValue = prevValueState.getOrElse(0)
      for (value <- currValues){
        newValue += value
      }
      // 返回option
      Option(newValue)
    }

    val end = traceLog.updateStateByKey(addFunc)

    end.foreachRDD(
            rdd => {
              rdd.foreachPartition(
                partitionOfRecords => {
                  var conn = MySqlPool.getJdbcConn()
                  try {
                    for (row <- partitionOfRecords) {
                      println("input data is " + row._1 + ", " + row._2)
                      val sql = "insert into trace_rpt(userid,count) values(" + row._1 + "," + row._2 + ")"
                      conn.prepareStatement(sql).executeUpdate()
                    }
                  }finally {
                    MySqlPool.releaseConn(conn)
                  }
//                  partitionOfRecords.foreach(pair => {
    //                val userId = pair._1
    //                val count = pair._2
    //                println("userID="+userId+",count="+count)
//                    println(pair)
//                  })
                }
              )
            }
        )

    // start sparkstreaming
    ssc.start()
    ssc.awaitTermination()
  }
}
