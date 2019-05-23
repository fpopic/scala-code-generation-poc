package com.github.fpopic.reflect

import OneParamWithExprPrintln

object MainOneParamWithExpr {

  def main(args: Array[String]): Unit = {
    val x = 10
    OneParamWithExprPrintln.helloDebug(x + 1)
  }

}
