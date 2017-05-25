package es.ugr.decsai.spark.sampling

import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import es.ugr.decsai.spark.sampling.common.CommonUtils
import org.apache.commons.lang.NotImplementedException
import es.ugr.decsai.spark.sampling.common.RDDPair
import es.ugr.decsai.spark.sampling.common.ExtendedRDDPair
import es.ugr.decsai.spark.sampling.common.RDDWithClasses

/**
 * @author SARA
 */
object runRUS {
  
  var num_pos: Long = 0;
  var num_neg: Long = 0;
  
  def main(arg: Array[String]) {

    var logger = Logger.getLogger(this.getClass())
    if (arg.length < 6) {
      logger.error("=> wrong parameters number")
      System.err.println("Parameters \n\t<path-to-header>\n\t<path-to-train>\n\t<number-of-partition>\n\t<name-of-majority-class>\n\t<name-of-minority-class>\n\t<pathOutput>")
      System.exit(1)
    }

    //Reading parameters
    val pathHeader = arg(0)
    val pathTrain = arg(1)
    val numPartition = arg(2).toInt
    val majclass = arg(3)
    val minclass = arg(4)  
    val pathOutput = arg(5)

    //Basic setup
    val jobName = "RUS-Spark" + "-" + numPartition

    //Spark Configuration
    val conf = new SparkConf().setAppName(jobName)
    val sc = new SparkContext(conf)

    logger.info("=> jobName \"" + jobName + "\"")
    logger.info("=> pathToHeader \"" + pathHeader + "\"")
    logger.info("=> pathToTrain \"" + pathTrain + "\"")
    logger.info("=> NumberPartition \"" + numPartition + "\"")
    logger.info("=> NameMajorityClass \"" + majclass + "\"")
    logger.info("=> NameMinorityClass \"" + minclass + "\"")
    logger.info("=> pathToOuput \"" + pathOutput + "\"")

    var inparam = new String
    inparam += "=> jobName \"" + jobName + "\"" + "\n"
    inparam += "=> pathToHeader \"" + pathHeader + "\"" + "\n"
    inparam += "=> pathToTrain \"" + pathTrain + "\"" + "\n"
    inparam += "=> NumberPartition \"" + numPartition + "\"" + "\n"
    inparam += "=> NameMajorityClass \"" + majclass + "\"" + "\n"
    inparam += "=> NameMinorityClass \"" + minclass + "\"" + "\n"
    inparam += "=> pathToOuput \"" + pathOutput + "\"" + "\n"

    logger.info("\nReading training file: " + pathTrain + " in " + numPartition + " partitions");
    
    val timeStart = System.nanoTime
    
    val trainRaw = sc.textFile(pathTrain: String, numPartition).cache    
    
    val undersample = runRUS(trainRaw, minclass, majclass)
    
    undersample.repartition(numPartition).coalesce(1, shuffle = true).saveAsTextFile(pathOutput)
    
    val timeEnd = System.nanoTime
    
    //OUTPUT
    var writerResult = new String
    writerResult += "Undersampling Time:\t\t" + (timeEnd - timeStart) / 1e9 + " seconds" + "\n"
   
    logger.info(writerResult)    

    println("Number of negative instances:" + num_neg)
    
    println("Number of positive instances:" + num_pos)
    
    println("Number of final instances:" + undersample.count())
  
  }
  
  private def isPosCountGreaterThanNegCount[T]: RDDPair[T] => ExtendedRDDPair[T] = _ match {
    case RDDPair(posRDD, negRDD) => {
      num_pos = posRDD.count()
      num_neg = negRDD.count()
      ExtendedRDDPair(num_pos > num_neg, posRDD, negRDD)
    }
  }
    
  private def doRUS[T]: ExtendedRDDPair[T] => RDD[T] = _ match {
    case ExtendedRDDPair(true, posRDD, negRDD) => negRDD.union(posRDD.sample(false, num_neg.toFloat/num_pos))
    case ExtendedRDDPair(false, posRDD, negRDD) => posRDD.union(negRDD.sample(false, num_pos.toFloat/num_neg))
  }
  
  private def apply[T]: RDDWithClasses[T] => RDD[T] = input => 
    (CommonUtils.filterByPosNeg andThen isPosCountGreaterThanNegCount[T] andThen doRUS[T])(input)
  
    
  def apply[T](sourceDataset: RDD[T], minclass: String, majclass: String): RDD[T] = 
    apply[T](RDDWithClasses(sourceDataset, minclass, majclass))

}

