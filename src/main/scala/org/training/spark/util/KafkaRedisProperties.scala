package org.training.spark.util

object KafkaRedisProperties {
  val REDIS_SERVER: String = "192.168.1.160"
  val REDIS_PORT: Int = 6379

  //  val KAFKA_SERVER: String = "chinahadoop-1"
  val KAFKA_ADDR: String = "192.168.2.160:9092,192.168.2.161:9092,192.168.2.162:9092"
//  val KAFKA_USER_TOPIC: String = "printtest"
  val KAFKA_RECO_TOPIC: String = "stockInfoNew"
}