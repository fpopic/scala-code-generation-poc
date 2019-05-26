package com.github.fpopic.scalameta

import com.google.api.services.bigquery.model.TableRow

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.meta._

trait TableRowConvertable[T] {
  def toTableRow(t: T): TableRow
}
object StringUtil {
  def uncapitalize(str: String): String = s"${str.head.toLower}${str.tail}"
}
@compileTimeOnly("enable macro paradise to expand macro annotations")
class ToTableRow extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {

    val (cls, companion) = defn match {
      case q"${cls: Defn.Class}; ${obj: Defn.Object}" =>
        (cls, obj)
      case q"${cls: Defn.Class};" =>
        // let's create companion object after class if it doesn't exist

        val typeName = cls.name // T

        val termName = Term.Name(cls.name.value) // string value of T
        val termNameLower = Term.Name(StringUtil.uncapitalize(cls.name.value))


        val newObj =
          q"""object $termName extends TableRowConvertable[${cls.name}] {

              import com.google.api.services.bigquery.model.TableRow

                def toTableRow($termNameLower: $typeName): TableRow = {
                  new TableRow()
                }

              }"""


        (cls, newObj)
      case _ =>
        abort("@ToTableRow must annotate a class")
    }




    // todo add
    //  val tr = new TableRow()
    //  tr.set(field_name, field_value)

    println("Out:")

    println(companion)


    q"$cls; $companion"
  }

}
