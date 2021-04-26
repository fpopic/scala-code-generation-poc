package com.github.fpopic.scalamacros.beam

import com.github.fpopic.scalamacros.MacrosHelper
import org.apache.beam.sdk.coders.{Coder, ListCoder, StringUtf8Coder, VarIntCoder}

import java.io.{InputStream, OutputStream}
import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

object DefMacroCoder extends LowPriorityMacros {

  def of[T](implicit c: Coder[T]): Coder[T] = c

  // HighPriorityMacros

  implicit val stringCoder: Coder[String] = StringUtf8Coder.of()

  implicit val intCoder: Coder[Int] = new Coder[Int] {

    private val baseCoder = VarIntCoder.of()

    override def encode(i: Int, os: OutputStream): Unit = baseCoder.encode(i, os)

    override def decode(is: InputStream): Int = baseCoder.decode(is)

    override def getCoderArguments: util.List[_ <: Coder[_]] = Collections.emptyList()

    override def verifyDeterministic(): Unit = baseCoder.verifyDeterministic()
  }

  implicit def listCoder[T](implicit c: Coder[T]): Coder[List[T]] = new Coder[List[T]] {

    private val baseCoder = ListCoder.of(c)

    override def encode(l: List[T], os: OutputStream): Unit = baseCoder.encode(l.asJava, os)

    override def decode(is: InputStream): List[T] = baseCoder.decode(is).asScala.toList // fix it

    override def getCoderArguments: util.List[_ <: Coder[_]] = Collections.emptyList()

    override def verifyDeterministic(): Unit = baseCoder.verifyDeterministic()
  }

  // invoke macro
  implicit def productCoder[P <: Product]: Coder[P] = macro materializeProductCoder[P]
}

//noinspection RedundantBlock
trait LowPriorityMacros {

  import scala.reflect.macros.blackbox

  def materializeProductCoder[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Coder[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]

    println(s"~> Building DefMacroCoder[${tpe}] ...")

    val helper = new MacrosHelper[c.type](c)

    // For each constructor field generate tree that repr. tuple ("name" -> value)
    val expressions: Seq[(c.Tree, c.Tree, c.Tree)] =
      helper.getPrimaryConstructorMembers(tpe).map { field =>
        val fieldType = field.typeSignature
        val fieldTerm = field.asTerm

        val innerType = field.typeSignature.finalResultType // inneryType for field is String (we call it F)
        val typeClassType: Type = typeOf(typeTag[Coder[innerType.type]]) //FIXME typeClassType is Coder[F], not T
        val appliedFieldType = appliedType(typeClassType, innerType) // we need Coder[String]

        val foundFieldImplicitCoder = c.inferImplicitValue(appliedFieldType)
        val fieldImplicitCoderExpression =
          if (foundFieldImplicitCoder.isEmpty) {
            c.abort(c.enclosingPosition, s"Implicit search failed for ${appliedFieldType}")
          } else {
            q"implicit val ${TermName(c.freshName())}: ${appliedFieldType} = ${foundFieldImplicitCoder}"
          }

        val fieldEncodeExpression = //FIXME use found coder here instead of implicitly
          q"_root_.scala.Predef.implicitly[Coder[${fieldType}]].encode(value.${fieldTerm}, os)"

        val fieldDecodeExpression = //FIXME user found coder here instead of implicitly
          q"${field.asTerm} = _root_.scala.Predef.implicitly[Coder[${fieldType}]].decode(is)"

        (fieldEncodeExpression, fieldDecodeExpression, fieldImplicitCoderExpression)
      }

    val implicitExpressions = expressions.map(_._3).distinct
    val coderEncodeExpresions = expressions.map(_._1)
    val coderDecodeExpresions = expressions.map(_._2)

    val classTypeName = TypeName(c.freshName())
    val coderExpression =
      q"""class ${classTypeName} extends org.apache.beam.sdk.coders.Coder[${tpe}] {

            ..${implicitExpressions}
            
            override def encode(value: ${tpe}, os: java.io.OutputStream): Unit = {
              ..${coderEncodeExpresions}
            }
            
            override def decode(is: java.io.InputStream): ${tpe} = {
              ${tpe.typeConstructor}(
                ..${coderDecodeExpresions}
              )
            }

            override def getCoderArguments: java.util.List[_ <: org.apache.beam.sdk.coders.Coder[_]] = java.util.Collections.emptyList

            override def verifyDeterministic(): Unit = ()
          }

          new ${classTypeName}
      """

    val ret = coderExpression
    println(ret)
    c.Expr[Coder[T]](ret)
  }

}
