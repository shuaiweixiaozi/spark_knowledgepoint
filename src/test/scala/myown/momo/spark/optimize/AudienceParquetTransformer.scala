package myown.momo.spark.optimize

import org.apache.spark.sql.{SQLContext,Row}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hzhb on 2017/8/21.
  */
object AudienceParquetTransformer {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("AudienceParquetTransformer").setMaster("local[3]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    /*
    step 1: Prepare RDDs
     */
    val DATA_PATH = ""
    val peopleRdd = sc.textFile(DATA_PATH)

    val nameSeq = scala.collection.mutable.ArrayBuffer.empty[String]
    nameSeq ++= Seq("first_name", "last_name", "email", "company", "job", "street_address"
      , "city", "state_abbr", "zipcode_plus4", "url", "phone_number", "user_agent", "user_name")
    for (i <- 0 to 328){
      nameSeq ++= Seq("letter_" + i,"number_" + i,"bool_" + i)
    }
    val dataTypeSeq = scala.collection.mutable.ArrayBuffer.empty[DataType]
    for(i <- 0 to 12){
      dataTypeSeq += StringType
    }
    for(i <- 0 to 328){
      dataTypeSeq ++= Seq(StringType,ShortType,BooleanType)
    }

    val schema = StructType((nameSeq zip dataTypeSeq).map(nameType=>StructField(nameType._1,nameType._2,true)))

    val peopleRowRdd = peopleRdd.map(_.split("\\|")).map{ p =>
      val row = scala.collection.mutable.ArrayBuffer.empty[scala.Any] ++ p
      for(i <- 13 to 999){
        if(i % 3 ==2) row.update(i,row(i).asInstanceOf[String].toShort)
        else if(i % 3 == 0) row.update(i,row(i).asInstanceOf[String].toBoolean)
      }
      Row(row:_*)
    }

    val peopleDF = sqlContext.createDataFrame(peopleRowRdd,schema)
    peopleDF.write.format("parquet").mode("overwrite").save("partuetDataPath")

    val txtDF = peopleDF
    txtDF.registerTempTable("txtTable")

    val parquetDF = sqlContext.read.parquet("partuetDataPath")
    parquetDF.registerTempTable("parquetTable")

    /*
    step 2:Compare
     */

    // optimazation: set spark.sql.shuffle.partitions=2
    val tableName = "parquetTable"
    val result = sqlContext.sql(s"select city,state_abbr,count(*) from ${tableName} where last_name not like 'w%'" +
      s"and email like '%com%' and letter_77 like 'r' and number_106 < 300 and bool_143 = true " +
      s"and letter_252 not like 'o' and number_311 > 400 group by city,state_abbr").explain()
  }




































































}
