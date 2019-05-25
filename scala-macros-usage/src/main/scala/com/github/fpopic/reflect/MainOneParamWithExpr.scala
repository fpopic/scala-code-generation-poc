package com.github.fpopic.reflect

object MainOneParamWithExpr {

  def main(args: Array[String]): Unit = {
    val x = 10
    OneParamWithExprPrintln.helloDebug(x + 1)
  }

}
