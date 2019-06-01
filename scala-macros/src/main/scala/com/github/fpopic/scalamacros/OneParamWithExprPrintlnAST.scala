package com.github.fpopic.scalamacros

import scala.language.experimental.macros

import scala.reflect.macros.blackbox

object OneParamWithExprPrintlnAST {

  def helloDebug(param: Any): Unit = macro helloDebugImpl

  def helloDebugImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    // show() Renders a representation of a reflection artifact as desugared Scala code.
    val paramRepr: String = show(param.tree)

    //FIXME println(paramRepr) // x.+(1)
    //FIXME println(param.actualType) // Int

    // We generate Tree manually using tree case classes
    val paramRepTree: Literal = Literal(Constant(paramRepr))

    // Let's make an expr of tree we just generated
    val paramRepExpr = c.Expr[String](paramRepTree)

    // Use `reify` to produce the abstract syntax tree representing a given Scala expression.
    // Use `splice` to turn an expr of type Expr[T] into a value of type T ** EVALUATES TREE **
    reify {
      // Print: expr = value
      // expr: we just splice the expr
      // value: we just splice the given param

      //FIXME println(paramRepExpr.splice + " = " + param.splice)
    }
  }

}
