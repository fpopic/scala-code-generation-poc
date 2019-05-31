package com.github.fpopic.reflect

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

// Quasiquotes Syntax:
//    https://docs.scala-lang.org/overviews/quasiquotes/syntax-summary.html

object Macros {

  // annottees -> all thing where we put @identity
  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val result = annottees.map(_.tree).toList match {
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
    c.Expr[Any](result)
  }

}

@compileTimeOnly("enable macro paradise to expand macro annotations")
class identity extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.impl
}
