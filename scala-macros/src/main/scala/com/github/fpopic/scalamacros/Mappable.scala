package com.github.fpopic.scalamacros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox


// 0. Define Type Class
trait Mappable[T] {

  def toMap(t: T): Map[String, Any]

}

object Mappable {

  // 3. Macro that generates for any case class Mappable implementation
  def materializeMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Mappable[T]] = {
    import c.universe._
    val tType: Type = weakTypeOf[T]

    def getPrimaryConstructorMembers(t: Type): Seq[c.Symbol] = t.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

    val pairs = getPrimaryConstructorMembers(tType).map { field =>
      val fName = field.name.decodedName.toString
      val fValue = field.name.toTermName
      val fType = field.typeSignature
      fType match {
        // primitives
        case t: Type if t =:= weakTypeOf[Int] => q"$fName -> (t.$fValue + 100)"
        //optional
        case t: Type if t =:= weakTypeOf[Option[Int]] => q"$fName -> t.$fValue.get"
        // arrays
        case t: Type if t =:= weakTypeOf[List[Int]] =>
          //          q"""import scala.collection.JavaConverters._
          //              $fName -> t.$fName.toArray"
          //           """
          q"$fName -> t.$fValue"
        // nested case class   TODO recursion
        case ntpe: Type =>
          val pairs = getPrimaryConstructorMembers(ntpe).foreach { field =>
            val nfName = field.name.decodedName.toString
            val nfValue = TermName(field.name.toString)
            val nfType = field.typeSignature

          }

      }
      q"$fName -> t.$fValue"
    }

    val ret =
      q"""new Mappable[$tType] {
             def toMap(t: $tType): Map[String, Any] = Map(..$pairs)
          }
       """

    println(s"Ret: $ret")

    c.Expr[Mappable[T]](ret)
  }

  // 2. Implicit method that triggers the macro
  implicit def materializeMappable[T]: Mappable[T] = macro materializeMappableImpl[T]

  // 1. Caller initiates type class implicits resolution
  def mapify[T](t: T)(implicit m: Mappable[T]): Map[String, Any] = m.toMap(t)

}
