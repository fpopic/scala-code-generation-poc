package com.github.fpopic.scalameta

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.meta._

@compileTimeOnly("enable macro paradise to expand macro annotations")
class TableRowConvertable extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    println(defn)
    println("1aaaaaaaaaaaaaaaaa")
    defn
  }

}
