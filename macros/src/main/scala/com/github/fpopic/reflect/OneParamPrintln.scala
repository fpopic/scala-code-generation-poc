package com.github.fpopic.reflect

import scala.reflect.macros.blackbox

object OneParamPrintln {

  def hello(param: Any): Unit = macro helloImpl

  def helloImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    // Use `reify` to produce the abstract syntax tree representing a given Scala expression.
    reify {
      println(param.splice)
    }
  }

}
