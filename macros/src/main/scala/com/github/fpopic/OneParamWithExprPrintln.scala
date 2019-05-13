package com.github.fpopic

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object OneParamWithExprPrintln {

  def helloDebug(param: Any): Unit = macro helloDebugImpl

  def helloDebugImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    // Use `reify` to produce the abstract syntax tree representing a given Scala expression.
    reify {
      println(param.splice)
    }
  }

}
