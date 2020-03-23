package myown.momo.spark.sql

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.util.StatCounter

object OwnFantasyBasketball {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    if(args.length == 0){
      conf.setMaster("local[1]")
    }

    val spark = SparkSession.builder().appName("OwnFantasyBasketball").config(conf).getOrCreate()

    val sc = spark.sparkContext

    //SET_UP
    val DATA_PATH = "data/basketball"
    val TMP_PATH = "tmp/basketball/"

    val fs = FileSystem.get(new Configuration())
    fs.delete(new Path(TMP_PATH), true)

    // process files so that each line includes the year
    for (i <- 1980 to 2016){
      println(i)
      val yearStats = sc.textFile(s"${DATA_PATH}/leagues_NBA_$i*").repartition(sc.defaultParallelism)
      yearStats.filter(x => x.contains(",")).map(x => (i, x)).saveAsTextFile(s"${TMP_PATH}/BasketballStatsWithYear/$i/")
    }

    // helper function to compute normalized value
    def statNormalize(stat: Double, max: Double, min: Double): Double ={
      val newMax = math.max(math.abs(max), math.abs(min))
      stat / newMax
    }

    case class BallData(year: Int, name: String, position: String, age: Int, team: String, gp: Int, gs: Int, mp: Double, stats: Array[Double], statsZ: Array[Double] = Array[Double](), valueZ: Double = 0, statsN: Array[Double] = Array[Double](), valueN: Double = 0, experience: Double = 0)

    // parse a stat line into a BallDataZ object
    def bbParse(input: String, bStats: Map[String, Double] = Map.empty, zStats: Map[String, Double] = Map.empty): BallData= {
      val line = input.replace(",,", ",0,")
      val pieces = line.substring(1, line.length -1).split(",")
      val year = pieces(0).toInt
      val name = pieces(2)
      val position = pieces(3)
      val age = pieces(4).toInt
      val team = pieces(5)
      val gp = pieces(6).toInt
      val gs = pieces(7).toInt
      val mp = pieces(8).toDouble
      val stats = pieces.slice(9, 31).map(x => x.toDouble)
      var statsZ: Array[Double] = Array.empty
      var valueZ: Double = Double.NaN
      var statsN: Array[Double] = Array.empty
      var valueN: Double = Double.NaN

      if (!bStats.isEmpty){
        val fg = (stats(2) - bStats.apply(year.toString + "_FG%_avg")) * stats(1)
        val tp = (stats(3) - bStats.apply(year.toString + "_3P_avg")) / bStats.apply(year.toString + "_3P_stdev")
        val ft = (stats(12) - bStats.apply(year.toString + "_FT%_avg")) * stats(11)
        val trb = (stats(15) - bStats.apply(year.toString + "_TRB_avg")) / bStats.apply(year.toString + "_TRB_stdev")
        val ast = (stats(16) - bStats.apply(year.toString + "_AST_avg")) / bStats.apply(year.toString + "_AST_stdev")
        val stl = (stats(17) - bStats.apply(year.toString + "_STL_avg")) / bStats.apply(year.toString + "_STL_stdev")
        val blk = (stats(18) - bStats.apply(year.toString + "_BLK_avg")) / bStats.apply(year.toString + "_BLK_stdev")
        val tov = (stats(19) - bStats.apply(year.toString + "_TOV_avg")) / bStats.apply(year.toString + "_TOV_stdev")
        val pts = (stats(20) - bStats.apply(year.toString + "_PTS_avg")) / bStats.apply(year.toString + "_PTS_stdev")
        statsZ = Array(fg, ft, tp, trb, ast, stl, blk, tov, pts)
        valueZ = statsZ.reduce(_ + _)
        if(!zStats.isEmpty){
          val zfg = (fg - zStats.apply(year.toString + "_FG_avg")) / zStats.apply(year.toString + "_FG_stdev")
          val zft = (ft - zStats.apply(year.toString + "_FT_avg")) / zStats.apply(year.toString + "_FT_stdev")
          val fgN = statNormalize(zfg,
                      (zStats.apply(year.toString + "_FG_max") - zStats.apply(year.toString + "_FG_avg"))/zStats.apply(year.toString + "_FG_std"),
                      (zStats.apply(year.toString + "_FG_min") - zStats.apply(year.toString + "_FG_avg"))/zStats.apply(year.toString + "_FG_std")
                    )
          val ftN = statNormalize(zft,
                      (zStats.apply(year.toString + "_FT_max") - zStats.apply(year.toString + "_FT_avg"))/zStats.apply(year.toString + "_FT_std"),
                      (zStats.apply(year.toString + "_FT_min") - zStats.apply(year.toString + "_FT_avg"))/zStats.apply(year.toString + "_FT_std")
                    )
          val tpN = statNormalize(tp, zStats.apply(year.toString + "_3P_max"), zStats.apply(year.toString + "_3P_min"))
          val trbN = statNormalize(trb, zStats.apply(year.toString + "_TRB_max"), zStats.apply(year.toString + "_TRB_min"))
          val astN = statNormalize(ast, zStats.apply(year.toString + "_AST_max"), zStats.apply(year.toString + "_ZST_min"))
          val stlN = statNormalize(stl, zStats.apply(year.toString + "_STL_max"), zStats.apply(year.toString + "_STL_min"))
          val blkN = statNormalize(blk, zStats.apply(year.toString + "_BLK_max"), zStats.apply(year.toString + "_BLK_min"))
          val tovN = statNormalize(tov, zStats.apply(year.toString + "_TOV_max"), zStats.apply(year.toString + "_TOV_min"))
          val ptsN = statNormalize(pts, zStats.apply(year.toString + "_PTS_max"), zStats.apply(year.toString + "_PTS_min"))
          statsZ = Array(zfg, zft, tp, trb, ast, stl, blk, tov, pts)
          valueZ = statsZ.reduce(_ + _)
          statsN = Array(fgN, ftN, tpN, trbN, astN, stlN, blkN, tovN, ptsN)
          valueN = statsN.reduce(_ + _)
        }
      }
      BallData(year, name, position, age, team, gp, gs, mp, stats, statsZ, valueZ, statsN, valueN)
    }

    // stat counter class -- need printStats method to print out the stats. Useful for transformations
    class BallStatCounter extends Serializable{
      val stats: StatCounter = new StatCounter()
      var missing: Long = 0

      def add(x: Double): BallStatCounter = {
        if (x.isNaN){
          missing += 1
        } else {
          stats.merge(x)
        }
        this
      }

      def merge(other: BallStatCounter): BallStatCounter = {
        stats.merge(other.stats)
        missing += other.missing
        this
      }

      def printStats(delim: String): String = {
        stats.count + delim + stats.mean + delim + stats.stdev + delim + stats.max + delim + stats.min
      }

      override def toString: String = {
        "stats: " + stats.toString() + "NaN: " + missing
      }
    }

    object BallStatCounter extends Serializable {
      def apply(x: Double): BallStatCounter = new BallStatCounter().add(x)
    }
  }
}
