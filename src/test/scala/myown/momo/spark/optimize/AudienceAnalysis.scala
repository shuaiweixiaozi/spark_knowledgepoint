package myown.momo.spark.optimize

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hzhb on 2017/8/21.
  */
object AudienceAnalysis {

  lazy val nameIndexMap = {
    val nameIndexMap = scala.collection.mutable.HashMap.empty[String, Int]
    val basicNames = Seq("first_name", "last_name", "email", "company", "job", "street_address"
      , "city", "state_abbr", "zipcode_plus4", "url", "phone_number", "user_agent", "user_name")
    nameIndexMap ++= basicNames zip (0 to 12)
    for (i <- 0 to 328){
      nameIndexMap ++= Seq(("letter_" + i,i * 3 + 13),("number_" + i, i * 3 + 14),("bool_" + i,i * 3 + 15))
    }
    nameIndexMap
  }

  def $(name:String):Int = nameIndexMap.getOrElse(name,-1)

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)

    /*
    step 1: Prepare RDDs
     */
    val DATA_PATH = ""
    val peopleTxtRdd = sc.textFile(DATA_PATH)

    /*
    s"select city,state_abbr,count(*) from ${tableName} where last_name not like 'w%'"+
    s"and email like '%com%' and letter_77 like 'r' and number_106 < 300 and bool_143 = true " +
    s"and letter_252 not like 'o' and number_311 > 400 group by city,state_abbr"
     */

    val resultRdd2 = peopleTxtRdd.map(_.split("\\|")).filter{p =>
      !p($("last_name")).matches("^w") &&
      p($("email")).matches(".*com.*") &&
      p($("letter_77")).equals("r") &&
      p($("number_106")).toInt < 300 &&
      !p($("bool_143")).toBoolean &&
      p($("letter_252")).equals("o") &&
      p($("number_311")).toInt > 400
    }.map{
      p => println("record:"+p($("city")))
        ((p($("city")),p($("state_abbr"))),1)
    }.reduceByKey(_+_,2)

    resultRdd2.collect()

    sc.stop()
  }
}
