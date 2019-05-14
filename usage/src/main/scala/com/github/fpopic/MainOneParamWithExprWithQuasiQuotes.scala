package com.github.fpopic

object MainOneParamWithExprWithQuasiQuotes {

  def main(args: Array[String]): Unit = {
    val x = 10
    OneParamWithExprPrintlnQuasiQuotes.helloDebug(x + 1)
  }

}
