package com.github.fpopic.reflect

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


object Macros {

  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val result = annottees.map(_.tree).toList match {
      case (md@ModuleDef(mods, name, Template(parents, self, body))) :: Nil =>
        println("mods:" + mods)
        println("name:" + name)
        println("parents:" + parents)
        println("self:" + self)
        println("body:" + body)
        md
    }
    c.Expr[Any](result)
  }

}

@compileTimeOnly("enable macro paradise to expand macro annotations")
class identity extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Macros.impl
}
