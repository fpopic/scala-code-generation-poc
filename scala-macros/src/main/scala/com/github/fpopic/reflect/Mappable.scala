package com.github.fpopic.reflect


import scala.language.experimental.macros
import scala.reflect.macros.blackbox

// 1
trait Mappable[T] {
  def toMap(t: T): Map[String, Any]
}

object Mappable {

  // initiates type class implicits resolution
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

    val toMapParams = fields.map { field: c.universe.Symbol =>
      q"${field.name.decodedName.toString} -> t.${TermName(field.name.toString)}"
    }

    val ret =
      q"""new Mappable[$tpe] {
             def toMap(t: $tpe): Map[String, Any] = Map(..$toMapParams)
          }
        """
    c.Expr[Mappable[T]](ret)
  }

}
