### Imb-sampling-ROS_and_RUS
Spark implementations of two data sampling methods (random oversampling and random undersampling) for imbalanced data.


## Example (Undersampling-Standalone):

Parameters 
>	"path-to-header"  
>	"path-to-train"  
>	"number-of-partition"  
>	"name-of-majority-class"  
>	"name-of-minority-class"  
>	"pathOutput"  


spark-submit --class org.apache.spark.mllib.sampling.runRUS Imb-sampling-1.0.jar hdfs://hadoop-master/datasets/data.header hdfs://hadoop-master/datasets/train.data 250 0 1 hdfs://hadoop-master/datasets/train-under.data

## Example (Oversampling-Standalone):


Parameters 
>	"path-to-header"  
>	"path-to-train"  
>	"number-of-partition"  
>	"number-of-repartition"  
>	"name-of-majority-class"  
>	"name-of-minority-class"  
>	"oversampling-rate"  
>	"pathOutput"  


spark-submit --class org.apache.spark.mllib.sampling.runROS Imb-sampling-1.0.jar hdfs://hadoop-master/datasets/data.header hdfs://hadoop-master/datasets/train.data 100 250 0 1 2.0 hdfs://hadoop-master/datasets/train-under.data

## Examples (library):

```Scala
import srio.org.apache.spark.mllib.sampling._
```
```Scala
// Undersampling RDD[LabeledPoint]

val LP:RDD[LabeledPoint]
val minoritaryClassValue:Double
val majoritaryClassValue:Double

val Result:RDD[LabeledPoint]  = runRUS.apply(LP, minoritaryClassValue, majoritaryClassValue)

```

```Scala
// Oversampling RDD[LabeledPoint]

val LP:RDD[LabeledPoint]
val minoritaryClassValue:Double
val majoritaryClassValue:Double
val percentage:Int

val Result:RDD[LabeledPoint]  = runROS.apply(LP, minoritaryClassValue, majoritaryClassValue,percentage)
```



```Scala
// Undersampling RDD[String]

val Str:RDD[String]
val minoritaryClassValue:String
val majoritaryClassValue:String

val Result:RDD[String]  = runRUS.apply(Str, minoritaryClassValue, majoritaryClassValue)

```

```Scala
// Oversampling RDD[String]

val Str:RDD[String]
val minoritaryClassValue:String
val majoritaryClassValue:String
val percentage:Int

val Result:RDD[String]  = runROS.apply(Str, minoritaryClassValue, majoritaryClassValue,percentage)
```

## Credits

Developed by: Sara del Río García (srio@decsai.ugr.es)

Maintained by: Sergio Ramírez (sramirez@decsai.ugr.es) / @sramirez