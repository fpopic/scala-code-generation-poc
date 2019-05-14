package com.github.fpopic

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object OneParamWithExprPrintlnQuasiQyotes {

  def helloDebug(param: Any): Unit = macro helloDebugImpl

  def helloDebugImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    // show() Renders a representation of a reflection artifact as desugared Scala code.
    val paramRepr: String = show(param.tree)

    println(paramRepr) // x.+(1)

    c.Expr[Unit](q"""println($paramRepr + " = " + $param)""")
  }

}
