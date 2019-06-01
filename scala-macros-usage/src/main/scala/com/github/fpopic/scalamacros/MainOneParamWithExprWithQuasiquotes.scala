package com.github.fpopic.scalamacros

object MainOneParamWithExprWithQuasiquotes {

  def main(args: Array[String]): Unit = {
    val x = 10
    OneParamWithExprPrintlnQuasiquotes.helloDebug(x + 1)
  }

}
