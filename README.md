### Imb-sampling-ROS_and_RUS
Spark implementations of two data sampling methods (random oversampling and random undersampling) for imbalanced data.


## Example (Undersampling):

Parameters 
>	"path-to-header"
>	"path-to-train"
>	"number-of-partition"
>	"name-of-majority-class"
>	"name-of-minority-class"
>	"pathOutput"


spark-submit --class org.apache.spark.mllib.sampling.runRUS Imb-sampling-1.0.jar hdfs://hadoop-master/datasets/data.header hdfs://hadoop-master/datasets/train.data 250 0 1 hdfs://hadoop-master/datasets/train-under.data

## Example (Oversampling):


Parameters 
>	"path-to-header"
>	"path-to-train"
>	"number-of-partition"
>	"number-of-repartition"
>	"name-of-majority-class"
>	"name-of-minority-class"
>	"oversampling-rate"
>	"pathOutput"


spark-submit --class org.apache.spark.mllib.sampling.runROS Imb-sampling-1.0.jar hdfs://hadoop-master/datasets/data.header hdfs://hadoop-master/datasets/train.data 100 250 0 1 2 hdfs://hadoop-master/datasets/train-under.data

## Credits

Developed by: Sara del Río García (srio@decsai.ugr.es)

Maintained by: Sergio Ramírez (sramirez@decsai.ugr.es) / @sramirez
