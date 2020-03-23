package com.momo.test

//import org.apache.hadoop.fs.Path
//import org.apache.hadoop.hbase.HBaseConfiguration
//import org.apache.hadoop.hbase.client.{Put, Result}
//import org.apache.hadoop.hbase.io.ImmutableBytesWritable
//import org.apache.hadoop.hbase.mapred.TableOutputFormat
//import org.apache.hadoop.hbase.mapreduce.TableInputFormat
//import org.apache.hadoop.hbase.util.Bytes
//import org.apache.hadoop.io.{LongWritable, Text}
//import org.apache.hadoop.mapred.lib.NLineInputFormat
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.hadoop.mapred.{FileInputFormat, JobConf}
//import org.apache.spark.rdd.{PairRDDFunctions, RDD}
//import org.apache.hadoop.conf.Configuration
/**
  * Created by hzhb on 2017/8/11.
  */
object TestHadoopRDD {

  def main(args: Array[String]): Unit = {
//    val sconf = new SparkConf().setAppName("TestHadoopRDD").setMaster("local[1]")
//    val sc = new SparkContext(sconf)
//
//    val jobConf = new JobConf()
//    val inputPath = "hdfs:///...."
//    FileInputFormat.setInputPaths(jobConf,inputPath)
//
//    jobConf.setInt("mapred.line.input.format.linespermap",100)
//
//    val inputFormatClass = classOf[NLineInputFormat]
//    val hadoopRdd = sc.hadoopRDD(jobConf,inputFormatClass,classOf[LongWritable],classOf[Text])
  }
}

object TestHBaseRDD {
  def main(args: Array[String]): Unit = {
//    //设置SparkContext
//    val sparkConf = new SparkConf().setAppName("TestHBaseRDD").setMaster("local[3]")
//    val sc = new SparkContext(sparkConf)
//
//    //设置hbase configuration
//    val hbaseConf = HBaseConfiguration.create()
//    hbaseConf.addResource(new Path("hbase-site.xml"))
//    hbaseConf.set(TableInputFormat.INPUT_TABLE,"TableName")
//
//    //创建hbase RDD
//    val hBaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],classOf[ImmutableBytesWritable],classOf[Result])
//
//    // 获取总行数
//    val count = hBaseRDD.count()
  }
}

//object TestSaveHBase{

//  def main(args: Array[String]): Unit = {
//    val hbaseConf = HBaseConfiguration.create()
//    hbaseConf.set("hbase.zookeeper.quorum","zkQuorum")
//
//    val sparkConf = new SparkConf().setAppName("TestSaveHBase").setMaster("local[3]")
//    val sc = new SparkContext(sparkConf)
//
////    val rdd = sc.sequenceFile("hdfs://...").map(x=>x)
////    rdd.foreach(line => saveToHBase(line,hbaseConf,"zkQuorum","hBaseTable"))
//  }

//  def saveToHBase(rdd:RDD[((String,Long),Int)],conf:Configuration,zkQuorum:String,tableName:String) ={
//    val jobConf = new JobConf(conf)
//    jobConf.set(TableOutputFormat.OUTPUT_TABLE,tableName)
//    jobConf.setOutputFormat(classOf[TableOutputFormat])
//    new PairRDDFunctions(rdd.map{case((metricId,timestamp),value) => createHBaseRow(metricId,timestamp,value)}).saveAsHadoopDataset(jobConf)
//  }

//  def createHBaseRow(metricId:String,timestamp:Long,value:Int)={
//    //    val record = new Put(Bytes.toBytes(metricId+"~"+timestamp))
//    //    record.add(Bytes.toBytes("metric"),Bytes.toBytes("col"),Bytes.toBytes(value.toString))
//    //    (new ImmutableBytesWritable,record)
//  }
//}
