package com.github.fpopic.scalameta

import com.google.api.services.bigquery.model.TableRow

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.collection.immutable
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

        // T
        val typeName: Type.Name = cls.name
        // Term(string value of T)
        val termName = Term.Name(cls.name.value)
        // t
        val termNameLower = Term.Name(StringUtil.uncapitalize(cls.name.value))

        // deconstruct (or just use from ADT) class to get members
        println("Members:")

        val x = cls.ctor.paramss.flatten.map { param: Term.Param =>
          val pName = param.name.value
          val pTermName: Term.Name = Term.Name(param.name.value)

          //val pType: Type = param.decltpe.get.asInstanceOf[Type]

          q"""tr.set($pName, $pTermName)"""
        }

        val newObj =
          q"""object $termName extends TableRowConvertable[${cls.name}] {

                import com.google.api.services.bigquery.model.TableRow

                def toTableRow($termNameLower: $typeName): TableRow = {
                  val tr = new TableRow()
                  ${Stat.x}
                  tr
                }

              }"""

        (cls, newObj)

      case _ =>
        abort("@ToTableRow must annotate a class")
    }




    //    val models = ???
    //
    //    companion.copy(
    //      templ = companion.templ.copy(
    //        // here i need to add method
    //        stats = Some(companion.templ.stats.getOrElse(Nil) ++ models)
    //      )
    //    )


    // todo add
    //  val tr = new TableRow()
    //  tr.set(field_name, field_value)

    println("Out:")

    println(companion)


    q"$cls; $companion"
  }

}
