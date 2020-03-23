package myown.momo.shetuanwajue

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{CoordinateMatrix, MatrixEntry}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hzhb on 2017/8/23.
  */
object SheTuanWaJue {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SheTuanWaJue").setMaster("local[3]")
    val sc = new SparkContext(conf)

    val data = sc.textFile("")
    val adjMatrixEntry = data.map(_.split(" ") match {
      case Array(id1,id2) => MatrixEntry(id1.toLong -1,id2.toLong -1 ,-1.0)
    })

    // 构造社交网络中的 邻接矩阵 S
    val adjMatrix = new CoordinateMatrix(adjMatrixEntry)
    println("Num of nodes=" + adjMatrix.numCols + ", Num of edges=" + data.count())

    //计算矩阵D的对角元素值
    val rows = adjMatrix.toIndexedRowMatrix().rows
    val diagMatrixEntry = rows.map{row => MatrixEntry(row.index,row.index,row.vector.toArray.sum)}

    //计算社交网络的拉普拉斯矩阵 L=D-S
    val laplaceMatrix = new CoordinateMatrix(sc.union(adjMatrixEntry,diagMatrixEntry))

    //计算拉普拉斯矩阵的特征列向量构成的矩阵（假设聚类个数为5）
    val eigenMatrix = laplaceMatrix.toRowMatrix.computePrincipalComponents(5)

    //特征列向量矩阵的行向量就是网络中节点对应的5维向量表示
    val nodes = eigenMatrix.transpose.toArray.grouped(5).toSeq
    val nodeSeq = nodes.map(node => Vectors.dense(node))
    val nodeVectors = sc.parallelize(nodeSeq)

    //最后求解节点在新向量表示下的K均值聚类结果：
    val clusters = new KMeans().setK(5).setMaxIterations(100).run(nodeVectors)
    val result = clusters.predict(nodeVectors).zipWithIndex().groupByKey().sortByKey()
    result.collect().foreach{c =>
      println("Nodes in cluster " + (c._1+1) + ": ")
      c._2.foreach(n => print(" " + n))
      println()
    }
  }
}
