package es.ugr.decsai.spark.sampling

import collection.mutable.Stack
import org.scalatest._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/*
 * 
 * "Well done. Here are the test results: You are a horrible person.
 * I'm serious, that's what it says: "A horrible person." We weren't
 * even testing for that. Don't let that horrible-person thing discourage
 * you. It's just a data point. If it makes you feel any better, science
 * has now validated your birth mother's decision to abandon you on a doorstep." - GLaDOS
 * 
 * 
            ,:/+/-
            /M/              .,-=;//;-
       .:/= ;MH/,    ,=/+%$XH@MM#@:
      -$##@+$###@H@MMM#######H:.    -/H#
 .,H@H@ X######@ -H#####@+-     -+H###@X
  .,@##H;      +XM##M/,     =%@###@X;-
X%-  :M##########$.    .:%M###@%:
M##H,   +H@@@$/-.  ,;$M###@%,          -
M####M=,,---,.-%%H####M$:          ,+@##
@##################@/.         :%H##@$-
M###############H,         ;HM##M$=
#################.    .=$M##M$=
################H..;XM##M$=          .:+
M###################@%=           =+@MH%
@################M/.          =+H#X%=
=+M##############M,       -/X#X+;.
  .;XM##########H=    ,/X#H+:,
     .=+HM######M+/+HM@+=.
         ,:/%XM####H/.
              ,.:=-.
 */



/*
 * This tests depends heavily on a random generator, causing some tests to fail sometimes
 */
class StackSpec extends FlatSpec {

  val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("Imb-sampling test"))
  val dataset = sc.parallelize(1 to 10).map { x => ""+x+","+(if(x>6) "min" else "maj") }
  
  
  "The random oversampling method" should "balance instances with percentage = 100" in {
    val oversampled = runROS(dataset, "min", "maj",100)
    
    val mincount = oversampled.filter { x => x.split(",").last.contentEquals("min") }.count()
    val majcount = oversampled.filter { x => x.split(",").last.contentEquals("maj") }.count()
    
    assert((mincount-majcount)<=1)
    
  }
    
  it should "double the minclass instances with percentage = 200" in {
    val oversampled = runROS(dataset, "min", "maj",200)
    
    val mincount = oversampled.filter { x => x.split(",").last.contentEquals("min") }.count()
    val majcount = oversampled.filter { x => x.split(",").last.contentEquals("maj") }.count()
    
    assert((mincount-majcount*2)<=2)
    
  }
  
  it should "triple the minclass instances with percentage = 300" in {
    val oversampled = runROS(dataset, "min", "maj",300)
    
    val mincount = oversampled.filter { x => x.split(",").last.contentEquals("min") }.count()
    val majcount = oversampled.filter { x => x.split(",").last.contentEquals("maj") }.count()
    
    assert((mincount-majcount*3)<=3)
  }
  
  it should "work well when fuzzying" in {
    (1 to 20).map(x => (Math.random()+1)*25).map(_.toInt).map { x =>
      val oversampled = runROS(dataset, "min", "maj",x*100)
    
      val mincount = oversampled.filter { x => x.split(",").last.contentEquals("min") }.count()
      val majcount = oversampled.filter { x => x.split(",").last.contentEquals("maj") }.count()
    
      assert((mincount-majcount*x)<=x)
    }
  }
  
  
  "The random oversampling method" should "balance the classes" in {
    val undersampled = runRUS(dataset, "min", "maj")
    
    val mincount = undersampled.filter { x => x.split(",").last.contentEquals("min") }.count()
    val majcount = undersampled.filter { x => x.split(",").last.contentEquals("maj") }.count()
    
    assert((mincount-majcount)<=1)
  }
}