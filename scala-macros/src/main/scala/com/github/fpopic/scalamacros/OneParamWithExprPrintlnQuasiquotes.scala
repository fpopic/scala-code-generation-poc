package com.github.fpopic.scalamacros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object OneParamWithExprPrintlnQuasiquotes {

  def helloDebug(param: Any): Unit = macro helloDebugImpl

  def helloDebugImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    // show() Renders a representation of a reflection artifact as desugared Scala code.
    val paramRepr: String = show(param.tree)

    //FIXME println(paramRepr) // x.+(1)

    c.Expr[Unit](q"""println($paramRepr + " = " + $param)""")
  }

}