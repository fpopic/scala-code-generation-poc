package com.github.fpopic.reflect

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

object NoParamsPrintln {

  // I want to generate method with 0 params and return type Unit
  def hello(): Unit = macro helloImpl

  // (Context)(params of method) : Expr[Type of method]
  // c.Expr is Scala code with some type
  def helloImpl(c: blackbox.Context)(): c.Expr[Unit] = {
    // needed to get AST objects
    import c.universe._

    println("I am just prof that I am run only during compile time!")

    // Use `reify` to produce the abstract syntax tree representing a given Scala expression.
    reify {
      println("Hello World")
    }
  }

}
