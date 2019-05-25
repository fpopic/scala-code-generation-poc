package com.github.fpopic.scalameta

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.meta._

@compileTimeOnly("enable macro paradise to expand macro annotations")
class TableRowConvertable extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {

    val (cls, companion) = defn match {
      case q"${cls: Defn.Class}; ${obj: Defn.Object}" =>
        (cls, obj)
      case q"${cls: Defn.Class};" =>
        // let's create companion object after class if it doesn't exist
        val newObj =
          q"""object ${Term.Name(cls.name.value)} {
                import com.google.api.services.bigquery.model.TableRow

                def toTableRow(x: ${cls.name}): TableRow = {
                  new TableRow()
                }

              }"""


        (cls, newObj)
      case _ =>
        abort("@TableRowConvertable must annotate a class")
    }




    // todo add
    //  val tr = new TableRow()
    //  tr.set(field_name, field_value)

    println("Out:")

    println(cls, companion)


    q"$cls; $companion"
  }

}
