package com.github.fpopic.scalamacros

import scala.reflect.macros.blackbox


// check byte code
// javap -c scala-macros-usage/target/scala-2.13.0-M3/classes/com/github/fpopic/scalamacros/A\$.class

/** Helper class that makes macro code compact */
class MacroHelper[C <: blackbox.Context](val c: C) {

  import c.universe._

  def getPrimaryConstructorMembers(tpe: c.Type): Seq[c.Symbol] =
    tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

}
