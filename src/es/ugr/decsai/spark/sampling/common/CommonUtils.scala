package es.ugr.decsai.spark.sampling.common

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.commons.lang.NotImplementedException

object CommonUtils {
  def checkIfClass(instance: Any, classValue: String): Boolean = instance match {
    case typedInstance:String => return (typedInstance.split(",").last.compareToIgnoreCase(classValue) == 0)
    case typedInstance:LabeledPoint => return (typedInstance.label ==  classValue)
    case _ => throw new NotImplementedException
  }
}