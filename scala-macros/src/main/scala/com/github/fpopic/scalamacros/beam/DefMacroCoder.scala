package com.github.fpopic.scalamacros.beam

import com.github.fpopic.scalamacros.MacrosHelper
import org.apache.beam.sdk.coders.{Coder, ListCoder, StringUtf8Coder, VarIntCoder}

import java.io.{InputStream, OutputStream}
import java.util
import java.util.Collections
import scala.jdk.CollectionConverters._

object DefMacroCoder extends LowPriorityMacros {

  // Coder[T] will be my typeclass
  def of[T](implicit c: Coder[T]): Coder[T] = c

  /* HighPriority */
  // some typeclass implementations
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

  /* LowPriority */
  implicit def productCoder[P <: Product]: Coder[P] = macro materializeProductCoder[P]
}

trait LowPriorityMacros {

  import scala.reflect.macros.blackbox

  def materializeProductCoder[P: c.WeakTypeTag](c: blackbox.Context): c.Expr[Coder[P]] = {
    import c.universe._
    val tpe = c.weakTypeOf[P]
    val helper = new MacrosHelper[c.type](c)

    val expressions =
      helper.getPrimaryConstructorMembers(tpe).map { field =>
        val fieldTerm = field.asTerm.name // e.g. value.s (for now just s)
        val fieldType = field.typeSignature.finalResultType // e.g. String

        val fieldCoderName = c.freshName(TermName("coder")) // e.g. give friendly name coder$...
        val fieldCoderInstance = // e.g. finds instance of Coder[String]
          c.typecheck(
            tree = q"""_root_.scala.Predef.implicitly[org.apache.beam.sdk.coders.Coder[${fieldType}]]""",
            silent = false
          )

        val fieldCoderExpression =
          q"private val ${fieldCoderName}: org.apache.beam.sdk.coders.Coder[${fieldType}] = ${fieldCoderInstance}"

        val fieldEncodeExpression =
          q"${fieldCoderName}.encode(value.${fieldTerm}, os)" // replace with full relative name (with dots) instead of value

        val fieldDecodeExpression =
          q"${field.asTerm} = ${fieldCoderName}.decode(is)"

        (fieldCoderExpression, fieldEncodeExpression, fieldDecodeExpression)
      }

    val fieldCodersExpression = expressions.map(_._1).distinct
    val coderEncodeExpresions = expressions.map(_._2)
    val coderDecodeExpresions = expressions.map(_._3)

    val coderExpression =
      q"""{
            new org.apache.beam.sdk.coders.Coder[${tpe}] {

              {import ${c.prefix}._}

              ..${fieldCodersExpression}

              override def encode(value: ${tpe}, os: java.io.OutputStream): _root_.scala.Unit = {
                ..${coderEncodeExpresions}
              }

              override def decode(is: java.io.InputStream): ${tpe} = {
                new ${tpe}(
                  ..${coderDecodeExpresions}
                )
              }

              override def getCoderArguments: java.util.List[_ <: org.apache.beam.sdk.coders.Coder[_]] = {
                java.util.Collections.emptyList
              }

              override def verifyDeterministic(): _root_.scala.Unit = ()
            }
          }
      """

    val ret = coderExpression
    c.Expr[Coder[P]](ret)
  }

}
