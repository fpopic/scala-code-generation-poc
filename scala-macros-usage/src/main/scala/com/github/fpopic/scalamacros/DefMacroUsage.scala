package com.github.fpopic.scalamacros

import java.sql.Timestamp

case class A(i: Int, oi: Option[Int], l: List[Int], b: B)

case class B(f: Float, ts: Timestamp)

object MappableIteratingUsage {

  def main(args: Array[String]): Unit = {

    import Mappable._

    val m: Map[String, Any] = mapify(
      A(
        i = 1,
        oi = Option(2),
        l = List(3, 4),
        b = B(
          f = 5.0f,
          ts = new Timestamp(10000L)
        )
      )
    )

    println(m)
  }
}
