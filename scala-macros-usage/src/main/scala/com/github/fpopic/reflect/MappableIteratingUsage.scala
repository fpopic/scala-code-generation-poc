package com.github.fpopic.reflect

case class MyCC(i: Int, s: String)

object MappableIteratingUsage {

  def main(args: Array[String]): Unit = {

    import Mappable._

    val m: Map[String, Any] = mapify(MyCC(1, "2"))

    println(m)
  }
}
