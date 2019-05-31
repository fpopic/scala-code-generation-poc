package com.github.fpopic.reflect

case class MyCC(i: Int, s: String, l: List[Double])

object MappableIteratingUsage {

  def main(args: Array[String]): Unit = {

    import Mappable._

    val m: Map[String, Any] = mapify(MyCC(1, "2", List(1.0, 2.0)))

    println(m)
  }
}
