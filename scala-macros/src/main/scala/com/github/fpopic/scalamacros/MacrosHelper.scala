package com.github.fpopic.scalamacros

import scala.reflect.macros.blackbox

// helper class that makes macro code compact
class MacrosHelper[C <: blackbox.Context](val c: C) {

  import c.universe._

  def getPrimaryConstructorMembers(tpe: c.Type): Seq[c.Symbol] =
    tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

}
