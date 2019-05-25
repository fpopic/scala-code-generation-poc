package com.github.fpopic.scalameta

import scala.annotation.StaticAnnotation
import scala.meta._

class TableRowConvertable extends StaticAnnotation {

  @inline def apply(defn: Any): Any = meta {
    defn
  }

}
