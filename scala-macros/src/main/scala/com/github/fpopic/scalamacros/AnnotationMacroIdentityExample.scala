//package com.github.fpopic.scalamacros
//
//import scala.annotation.{StaticAnnotation, compileTimeOnly}
//import scala.reflect.api.Trees
//import scala.reflect.macros.blackbox
//
//// Quasiquotes Syntax:
////    https://docs.scala-lang.org/overviews/quasiquotes/syntax-summary.html
//
//// 1. Provide macro impl.
//object Macros {
//
//  // annottees -> all thing where we put @identity
//  def impl(c: blackbox.Context)(annottees: c.Tree*): c.Tree = {
//    import c.universe._
//    val helper = new MacrosHelper[c.type](c)
//
//    println("\nAnnotationMacro:")
//
//    // To extract annotation parameter
//    val i: Int = c.prefix.tree match {
//      case q @ q"new identity(i=$i)" =>
//        helper.evalTree[Int](i)
//      case _ => 0
//    }
//
//    println(s"Parameter i:$i")
//
//    val inputs = annottees.toList
//
//    val (annottee, expandees) = inputs match {
//      case (param: ValDef) :: (rest @ _ :: _) =>
//        (param, rest)
//
//      case (param: TypeDef) :: (rest @ _ :: _) =>
//        (param, rest)
//
//      case _ =>
//        (EmptyTree, inputs)
//    }
//
//    println("XX" + (annottee, expandees))
//
//    Block(expandees, Literal(Constant(())))
//  }
//
//}
//
//// 0. Define anotation
//@compileTimeOnly("enable macro paradise to expand macro annotations")
//class identity(i: Int) extends StaticAnnotation {
//  def macroTransform(annottees: Any*): Any = macro Macros.impl
//}
