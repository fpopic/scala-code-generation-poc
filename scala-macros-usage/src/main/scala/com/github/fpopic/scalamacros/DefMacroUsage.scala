package com.github.fpopic.scalamacros

case class N(j: Int, @identity(i = 5) k: Int)

case class A(i: Int, s: Some[Int], l: List[Int], n: N)


object DefMacroUsage {

  def main(args: Array[String]): Unit = {

    import ToMap._

    //val x = ToMap[A].toMap(A(1, Some(1), List(1), N(1, 1)))

    val a: Map[String, Any] =
      mapify(
        A(
          i = 1,
          s = Some(2),
          l = List(3, 4),
          n = N(
            j = 5,
            k = 7
          )
        )
      )

    println(a)

  }
}
