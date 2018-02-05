# Imbalanced sampling: ROS and RUS
Spark implementations of two data sampling methods (random oversampling and random undersampling) for imbalanced data.

---

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

The following examples only work for RDD[T], where `T` is one of:

* String
* kNN_IS's utils.keel.KeelParser

In another case, a NotImplementedException will be thrown.


```Scala
import es.ugr.decsai.spark.sampling._
```

#### Undersampling

`runRUS` balances instances by class, removing majority class instances.


```Scala
// Loaded RDD:
val dataset:RDD[T]

// Class names
val minoritaryClassValue:String = "min"
val majoritaryClassValue:String = "maj"

val result:RDD[T]  = runRUS(dataset, minoritaryClassValue, majoritaryClassValue)
```

#### Oversampling

`runROS` uses random oversampling to set the number of minority class instances equal to the `percentage`% of the majority class instances 

```Scala

//Loaded RDD:
val dataset:RDD[T]

// Classes names
val minoritaryClassValue:String = "min"
val majoritaryClassValue:String = "maj"

// Oversampling rate (percentage of minority class instances over majority class ones after ROS)
val percentage:Int = 200

val result:RDD[T]  = runROS(dataset, minoritaryClassValue, majoritaryClassValue,percentage)
```

## Credits

Developed by: Sara del Río García (srio@decsai.ugr.es)

Maintained by: Sergio Ramírez (sramirez@decsai.ugr.es) / @sramirez

Conversion to library & functional improvements by: Daniel Trujillo Viedma (dtviedma@ujaen.es / [@gDanix](https://github.com/gDanix/))

KeelParser's LabeledPoint adaptation by: Mauricio Orellana Grande (m.orellana.grande@gmail.com / [@smog2010](https://github.com/smog2010/))
