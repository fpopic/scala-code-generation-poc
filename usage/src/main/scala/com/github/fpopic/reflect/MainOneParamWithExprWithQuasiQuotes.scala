package com.github.fpopic.reflect

import OneParamWithExprPrintlnQuasiQuotes

object MainOneParamWithExprWithQuasiQuotes {

  def main(args: Array[String]): Unit = {
    val x = 10
    OneParamWithExprPrintlnQuasiQuotes.helloDebug(x + 1)
  }

}
