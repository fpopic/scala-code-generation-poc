package com.github.fpopic.scalamacros

case class N(j: Int, k: Int)

case class A(i: Int, s: Some[Int], /*l: List[Int],*/ n: N)

//case class B(i: Int, l: List[Int], n: N)


object DefMacroUsage {

  def main(args: Array[String]): Unit = {

    import ToMap._

    val a: Map[String, Any] = mapify(
      A(
        i = 1,
        s = Some(2),
        //        l = List(3, 4),
        n = N(
          j = 5,
          k = 7
        )
      )
    )

    println(a)

  }
}
