
import scala.io.Source

/**
  * Created by surdamodaran on 8/24/16.
  *
  * Calculates Summation of [(a+b)/2 * (t1 - t0) + (b+c)/2 * (t2 - t1) + (c+d)/2 * (t3 -t2) + ...]
  *
  * This gives the area of the Sine wave (trapezoid) with 99.9% accuracy
  *
  */

object ScalaJsonParserForCalculatingArea extends App {

  def main(args:Array[String]){

        val lines = Source.fromFile("/ScalaJsonParser/src/main/resources/Input.json").getLines.mkString
        val newLines = lines.replaceFirst("\\[","").dropRight(1).mkString
        val jsonString  = scala.util.parsing.json.JSON.parseFull(newLines)
        jsonString match {
          case Some(map: Map[Any, Any]) => {
            val someList: List[Any] = flatten(map.get("DataKV").toList)
            someList.foreach(println(_))


            //Accomplish addition of keys and produce a new list
            // (a+b)/2
            val keyList = someList.indices.filter(_ % 2 == 0).map(someList(_))
            keyList.map(x => x.asInstanceOf[scala.Double])
            keyList.foreach(println(_))
            val contiguousSum: List[scala.Double] = computeContiguousSum(keyList.toList)
            println("Computing contiguous sum  :: " + contiguousSum)


            //Accomplish difference of values and produce a new list
            // (t0 - t1)
            val valueList = someList.indices.filter(_ % 2 != 0).map(someList(_))
            valueList.map(x => x.asInstanceOf[scala.Double])
            val contiguousDifference: List[scala.Double] = computeContiguousDifference(valueList.toList)

            println("Computing contiguous difference :: " + contiguousDifference)
            println("contiguousDifference.getClass : " + contiguousDifference.getClass)


            //Accomplish multiplication of two lists (sum list and differnce list) and produce a new list
            //  Summation - (a+b)/ 2 * (t1 - t0)
            val zippedElements = contiguousSum.zipAll(contiguousDifference, 0, 0).map { case (x, y) => x.asInstanceOf[scala.Double] * y.asInstanceOf[scala.Double] }

            println("zippedElements ::: " + zippedElements)

            //Accomplish summation
            val numOfElements = zippedElements.size
            val sum = zippedElements.map(x => x.asInstanceOf[scala.Double]) reduceLeft (_ + _)
            val avg = sum / numOfElements
            println("SUM of ::: " + numOfElements + " elements :::" + sum)
            println("AVG of ::: " + numOfElements + " elements :::" + avg)
          }
          case None => println("No match and exception")
          case _ => println("Structure not defined ")
    }
  }

  def flatten(l: List[_]): List[Any] = l match { case Nil => Nil case (head: List[_]) :: tail => flatten(head) ::: flatten(tail) case head :: tail => head :: flatten(tail)}

  /**
    * Function to process diffences betwween contiguous elements and produce a list
    *  (t1 - t0)
    *
    * @param listToCompute
    * @return
    */
  def computeContiguousDifference(listToCompute : List[Any]) = {
    val difference = listToCompute.map(x=> x.asInstanceOf[scala.Double]).sliding(2).map{case List(x, y) => y - x}.toList
    //println(difference)
    difference
  }

  /**
    * Function to process sum of contigous elements and divide by 2 and produce a list
    *  (a+b) / 2
    *
    * @param listToCompute
    * @return
    */
  def computeContiguousSum(listToCompute : List[Any]) = {
    val sum = listToCompute.map(x => x.asInstanceOf[scala.Double]).sliding(2).map{case List(x,y) => (x + y)/2}.toList
    //println(sum)
    sum
  }
}
