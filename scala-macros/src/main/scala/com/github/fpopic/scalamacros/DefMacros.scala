package com.github.fpopic.scalamacros

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.languageFeature.experimental.macros
import scala.reflect.macros.blackbox

// 0. Define Type Class
trait ToMap[T] {

  def toMap(t: T): Map[String, Any]

}

object ToMap extends ToMapLowPriorityImplicits {
  // 2. Implicit method that triggers the macro

  // HighPriorityImplicits
  // I can now only make implicits for whole case class but not for fields,
  // for them i need a new type class or in pattern matching in mapEntries code add cases

  // LowPriorityMacros
  implicit def caseClassMappable[T]: ToMap[T] = macro materializeMappableImpl[T]

  // 1. Caller initiates type class implicits resolution
  def mapify[T](t: T)(implicit m: ToMap[T]): Map[String, Any] = m.toMap(t)
}

trait ToMapLowPriorityImplicits {

  // 3. Macro that generates for any case class ToMap implementation
  def materializeMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[ToMap[T]] = {
    import c.universe._
    val helper = new MacrosHelper[c.type](c)

    val tpe = weakTypeOf[T]
    println(s"ToMap: $tpe")

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
          case t if t <:< weakTypeOf[Option[_]] =>
            // FIXME
            q"""if ($fTerm.isDefined)
                  $fName -> $fTerm.get
                else ""
              """
          case t if t =:= weakTypeOf[List[Int]] =>
            println(s"$fName : $fType")
            q"$fName -> t.$fTerm"
          // in case field is nested case class
          case n if n.baseClasses.contains(weakTypeOf[Product].typeSymbol) =>
            println(s"$fName : $fType")
            q"$fName -> mapify(t.$fTerm)"
          case t =>
            try {
              // default ones
            }
            catch {
              case e: Exception =>
                c.abort(c.enclosingPosition, s"Type $t is not supported.")
            }
            q""
        }
      }

    val ret =
      q"""new ToMap[$tpe] {
             def toMap(t: $tpe): Map[String, Any] = Map(..$mapEntries)
          }
       """

    println(s"Ret: $ret")

    c.Expr[ToMap[T]](ret)
  }

}



// check byte code
// javap -c scala-macros-usage/target/scala-2.13.0-M3/classes/com/github/fpopic/scalamacros/A\$.class
