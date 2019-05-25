package com.github.fpopic.scalameta

@TableRowConvertable
case class XXX(a: Int, b: List[String])

object Main {

  def main(args: Array[String]): Unit = {
    println(
      XXX.toTableRow(
        XXX(11111, List("1", "2"))
      )
    )
  }
}
