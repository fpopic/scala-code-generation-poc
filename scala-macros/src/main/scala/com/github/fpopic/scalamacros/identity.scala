package com.github.fpopic.scalamacros

import sun.reflect.generics.tree.Tree

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

// Now, the macroTransform macro is supposed to take a list of untyped annottees

//  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any]

// (in the signature their type is represented as Any for the lack of better notion in Scala)
// and produce one or several results (a single result can be returned as is, multiple results
// have to be wrapped in a Block for the lack of better notion in the reflection API).


// Quasiquotes Syntax:
//    https://docs.scala-lang.org/overviews/quasiquotes/syntax-summary.html

object Macros {

  // annottees -> all thing where we put @identity
  def impl(c: blackbox.Context)(annottees: c.Tree*): c.Tree = {
    import c.universe._
    val ret = annottees.toList match {
      case (cd@q"""$mods class $tpname[..$tparams] $ctorMods(...$paramss)
                extends { ..$earlydefns } with ..$parents { $self => ..$stats }""") :: Nil =>

        //FIXME println("Quasiquote: Class:")

        paramss foreach {
          case m: MethodSymbol if m.isCaseAccessor =>
          //FIXME println(m.name)
          case x =>
          //FIXME println("X:" + x)
        }

        cd
    }

    println("identiy: " + ret)
    ret
  }

}

@compileTimeOnly("enable macro paradise to expand macro annotations")
class identity extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.impl
}
