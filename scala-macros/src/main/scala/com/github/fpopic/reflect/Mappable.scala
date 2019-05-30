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

  // 3 Macro that generates for each call map object
  def materializeMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Mappable[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

    val pairs = fields.map { field =>
      val name = field.name.decodedName.toString
      val value = TermName(field.name.toString)
      q"$name -> t.$value"
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
