package org.apache.spark.mllib.sampling

import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import scala.collection.mutable.ListBuffer
import java.io.IOException
import java.io.File
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import java.util.ArrayList
import com.typesafe.config.ConfigFactory
import java.io.PrintWriter
import org.apache.spark.rdd.MapPartitionsRDD
import org.apache.spark.rdd.RDD
import scala.util.Random

/**
 * @author SARA
 */
object runROS {
  def main(arg: Array[String]) {

    var logger = Logger.getLogger(this.getClass())
    if (arg.length < 8) {
      logger.error("=> wrong parameters number")
      System.err.println("Parameters \n\t<path-to-header>\n\t<path-to-train>\n\t<number-of-partition>\n\t<number-of-repartition>\n\t<name-of-majority-class>\n\t<name-of-minority-class>\n\t<oversampling-rate>\n\t<pathOutput>")
      System.exit(1)
    }

    //Reading parameters
    val pathHeader = arg(0)
    val pathTrain = arg(1)
    val numPartition = arg(2).toInt
    val numRePartition = arg(3).toInt
    val majclass = arg(4)
    val minclass = arg(5)
    val overRate = arg(6).toDouble
    val pathOutput = arg(7)

    //Basic setup
    val jobName = "ROS-Spark" + "-" + numPartition + "Parts-" + overRate + "OverRate"

    //Spark Configuration
    val conf = new SparkConf().setAppName(jobName)
    val sc = new SparkContext(conf)

    logger.info("=> jobName \"" + jobName + "\"")
    logger.info("=> pathToHeader \"" + pathHeader + "\"")
    logger.info("=> pathToTrain \"" + pathTrain + "\"")
    logger.info("=> NumberPartition \"" + numPartition + "\"")
    logger.info("=> NumberRePartition \"" + numPartition + "\"")
    logger.info("=> NameMajorityClass \"" + majclass + "\"")
    logger.info("=> NameMinorityClass \"" + minclass + "\"")
    logger.info("=> OversamplingRate \"" + overRate + "\"")
    logger.info("=> pathToOuput \"" + pathOutput + "\"")

    var inparam = new String
    inparam += "=> jobName \"" + jobName + "\"" + "\n"
    inparam += "=> pathToHeader \"" + pathHeader + "\"" + "\n"
    inparam += "=> pathToTrain \"" + pathTrain + "\"" + "\n"
    inparam += "=> NumberPartition \"" + numPartition + "\"" + "\n"
    inparam += "=> NumberRePartition \"" + numPartition + "\"" + "\n"
    inparam += "=> NameMajorityClass \"" + majclass + "\"" + "\n"
    inparam += "=> NameMinorityClass \"" + minclass + "\"" + "\n"
    inparam += "=> OversamplingRate \"" + overRate + "\"" + "\n"
    inparam += "=> pathToOuput \"" + pathOutput + "\"" + "\n"

    logger.info("\nReading training file: " + pathTrain + " in " + numPartition + " partitions");

    val timeStart = System.nanoTime

    val trainRaw = sc.textFile(pathTrain: String, numPartition).cache

    var oversample: RDD[String]= null
    var fraction = 0.0

    val train_positive = trainRaw.filter(line => line.split(",").last.compareToIgnoreCase(minclass) == 0)
    val train_negative = trainRaw.filter(line => line.split(",").last.compareToIgnoreCase(majclass) == 0)

    val num_neg = train_negative.count()
    val num_pos = train_positive.count()

    println("OverRate: " + overRate)

    if (num_pos > num_neg){
      oversample = train_positive.union(train_negative.sample(true, overRate, 1234))

    }else{
      oversample = train_negative.union(train_positive.sample(true, overRate, 1234))
    }

    oversample.repartition(numRePartition).coalesce(1, shuffle = true).saveAsTextFile(pathOutput)

    val timeEnd = System.nanoTime

    //OUTPUT
    var writerResult = new String
    writerResult += "Oversampling Time:\t\t" + (timeEnd - timeStart) / 1e9 + " seconds" + "\n"

    logger.info(writerResult)

    println("Number of negative instances:" + num_neg)

    println("Number of positive instances:" + num_pos)

    println("Number of final instances:" + oversample.count())

  }

}
