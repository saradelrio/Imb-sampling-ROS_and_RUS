package es.ugr.decsai.spark.sampling.common

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.commons.lang.NotImplementedException
import org.apache.spark.rdd.RDD

case class RDDPair[T](first: RDD[T], second: RDD[T])
case class ExtendedRDDPair[T](mark: Boolean, first: RDD[T], second: RDD[T])
case class RDDWithClasses[T](dataset: RDD[T], minclass: String, majclass: String)

object CommonUtils {
  
  
  
  def checkIfClass(instance: Any, classValue: String): Boolean = instance match {
    case typedInstance:String => return (typedInstance.split(",").last.compareToIgnoreCase(classValue) == 0)
    case typedInstance:LabeledPoint => return (typedInstance.label ==  classValue)
    case _ => throw new NotImplementedException
  }
  
  def filterByPosNeg[T]: RDDWithClasses[T] => RDDPair[T] = _ match {
    case RDDWithClasses(dataset, minclass, majclass) => RDDPair[T](dataset.filter(checkIfClass(_,minclass)),dataset.filter(checkIfClass(_,majclass)))
  }
}