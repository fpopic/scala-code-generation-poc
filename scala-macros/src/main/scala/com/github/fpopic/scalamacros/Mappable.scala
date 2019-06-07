package com.github.fpopic.scalamacros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

// 0. Define Type Class
trait Mappable[T] {

  def toMap(t: T): Map[String, Any]

}

object Mappable extends MappableLowPriorityImplicits {

  // 2. Implicit method that triggers the macro

  // HighPriorityImplicits

  // LowPriorityMacros
  implicit def caseClassMappable[T]: Mappable[T] = macro materializeMappableImpl[T]

  // 1. Caller initiates type class implicits resolution
  def mapify[T](t: T)(implicit m: Mappable[T]): Map[String, Any] = m.toMap(t)
}

trait MappableLowPriorityImplicits {

  // 3. Macro that generates for any case class Mappable implementation
  def materializeMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Mappable[T]] = {
    import c.universe._
    val helper = new Helper[c.type](c)

    val tpe: c.universe.Type = weakTypeOf[T] //A

    println(s"Mappable: $tpe")

    // For each constructor field genrate tree that repr. tuple ("name" -> value)
    val mapEntries: Seq[c.Tree] =
      helper.getPrimaryConstructorMembers(tpe).map { field =>
        val fName = field.name.decodedName.toString
        val fTerm = field.name.toTermName
        // doesn't work with tType.decl(field.name).typeSignature
        val fType = field.typeSignature
        fType match {
          case t if t =:= weakTypeOf[Int] =>
            println(s"$fName : $fType")
            q"$fName -> (t.$fTerm + 100)"
          case t if t =:= weakTypeOf[Some[Int]] =>
            println(s"$fName : $fType")
            q"$fName -> t.$fTerm.get"
          case t if t =:= weakTypeOf[List[Int]] =>
            println(s"$fName : $fType")
            q"$fName -> t.$fTerm"
          // in case field is nested case class
          case n if n.baseClasses.contains(weakTypeOf[Product].typeSymbol) =>
            println(s"$fName : $fType")
            q"$fName -> mapify(t.$fTerm)"
          case t =>
            c.abort(c.enclosingPosition, s"Type $t not supported.")
        }
      }

    val ret =
      q"""new Mappable[$tpe] {
             def toMap(t: $tpe): Map[String, Any] = Map(..$mapEntries)
          }
       """

    println(s"Ret: $ret")

    c.Expr[Mappable[T]](ret)
  }

}

// helper class that makes macro code compact
class Helper[C <: blackbox.Context](val c: C) {

  import c.universe._

  def getPrimaryConstructorMembers(tpe: c.Type): Seq[c.Symbol] =
    tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

}

// check byte code
// javap -c scala-macros-usage/target/scala-2.13.0-M3/classes/com/github/fpopic/scalamacros/A\$.class
