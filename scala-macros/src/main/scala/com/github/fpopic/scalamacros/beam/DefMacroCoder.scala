package com.github.fpopic.scalamacros.beam

import org.apache.beam.sdk.coders.Coder

trait DefMacroCoder[T] extends Coder[T]

object DefMacroCoder extends LowPriorityMacros {

  def of[T](implicit c: DefMacroCoder[T]): DefMacroCoder[T] = c

  // TODO Here add HighPriorityMacros

  implicit def productCoder[P <: Product]: DefMacroCoder[P] = macro materializeProductCoder[P]
}

trait LowPriorityMacros {

  import scala.reflect.macros.blackbox

  def materializeProductCoder[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[DefMacroCoder[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]
    println(s"~> Building DefMacroCoder[${tpe}] ...")

    // TODO do the macro magic

    val ret = q""
    c.Expr[DefMacroCoder[T]](ret)
  }

}
