package com.github.fpopic.scalamacros

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


// Quasiquotes Syntax:
//    https://docs.scala-lang.org/overviews/quasiquotes/syntax-summary.html

// 1. Provide macro impl.
object Macros {

  // annottees -> all thing where we put @identity
  def impl(c: blackbox.Context)(annottees: c.Tree*): c.Tree = {
    import c.universe._
    val ret = annottees.toList match {
      case (cd@q"""$mods class $tpname[..$tparams] $ctorMods(...$paramss)
                extends { ..$earlydefns } with ..$parents { $self => ..$stats }""") :: Nil =>

        //println("Quasiquote: Class:")

        paramss foreach {
          case m: MethodSymbol if m.isCaseAccessor => //println(m.name)
          case x => //println("X:" + x)
        }

        cd
    }

    println("identiy: " + ret)
    ret
  }

}

// 0. Define anotation
@compileTimeOnly("enable macro paradise to expand macro annotations")
class identity extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.impl
}
