//package com.github.fpopic.reflect
//
//import scala.language.experimental.macros
//import scala.reflect.macros.blackbox
//
//
//trait Mapable[T] {
//  def toMap(t: T): Map[String, Any]
//}
//
//object Macros {
//
//  def impl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Mapable[T]] = {
//    import c.universe._
//    val tpe = weakTypeOf[T] // type evidence
//
//    tpe match {
//      case t if t =:= weakTypeOf[String] => q"def toMap"
//
//
//    }
//
//    val ret =
//      q"""new Mapable[(String ,$tpe]{
//
//      }
//
//       """
//
//    c.Expr[Mapable[T]](ret)
//  }
//}
//
//object Mapable {
//
//  // macro should provide serializer for any class
//  implicit def materializeMap[T]: Mapable[T] = macro Macros.impl
//
//  // we can explicitly define primitives  (non macro)
//  implicit def stringToMapEntry: Mapable[(String, String)] = new Mapable[(String, String)] {
//    def toMap(t: (String, String)): Map[String, Any] = {
//      ()
//    }
//  }
//}
