package com.github.fpopic.scalameta

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.meta._

class TableRowConvertable extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    println(defn)
    defn
  }

}
