package com.github.fpopic.scalamacros

object MainOneParamWithExprAST {

  def main(args: Array[String]): Unit = {
    val x = 10
    OneParamWithExprPrintlnAST.helloDebug(x + 1)
  }

}
