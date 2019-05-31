package com.github.fpopic.reflect


import scala.language.experimental.macros
import scala.reflect.macros.blackbox

// 1 Define Type Class
trait Mappable[T] {
  def toMap(t: T): Map[String, Any]
}

object Mappable {

  // Initiates type class implicits resolution
  def mapify[T](t: T)(implicit m: Mappable[T]): Map[String, Any] = m.toMap(t)

  // 2 Implicit method that triggers the macro
  implicit def materializeMappable[T]: Mappable[T] = macro MacroImpl.materializeMappableImpl[T]

}

object MacroImpl {

  // 3 Macro that generates for any case class Mappable implementation
  def materializeMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Mappable[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

    val pairs = fields.map { field =>
      val fName = field.name.decodedName.toString
      val fValue = TermName(field.name.toString)
      val fType = field.typeSignature
      fType match {
        case t if t =:= weakTypeOf[Int] => q"$fName -> (t.$fValue + 100)"
        case _ => q"$fName -> t.$fValue"
      }
    }

    val ret =
      q"""new Mappable[$tpe] {
             def toMap(t: $tpe): Map[String, Any] = Map(..$pairs)
          }
       """

    println(s"Ret: $ret")

    c.Expr[Mappable[T]](ret)
  }

}
