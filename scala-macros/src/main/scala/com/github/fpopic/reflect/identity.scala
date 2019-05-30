package com.github.fpopic.reflect

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


object Macros {

  // annottees -> all thing where we put @identity
  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val result = annottees.map(_.tree).toList match {
      case (md@ModuleDef(mods, name, Template(parents, self, body))) :: Nil =>
        println("Object:")
        println(md)
        md
      case (vd@ValDef(mods, name, type_, rhs)) :: Nil =>
        println("Val:")
        println(vd)
        vd
      case (cd@ClassDef(mods, name, types, body)) :: Nil =>
        println("Class:")
        println(cd)
        cd
    }
    c.Expr[Any](result)
  }

}

@compileTimeOnly("enable macro paradise to expand macro annotations")
class identity extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.impl
}
