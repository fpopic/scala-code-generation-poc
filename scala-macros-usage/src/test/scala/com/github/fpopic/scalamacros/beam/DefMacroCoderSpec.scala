package com.github.fpopic.scalamacros.beam

import org.apache.beam.sdk.coders.Coder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

case class Pojo(s: String, i: Int, l: List[Int], s2: String)
case class ContainsNested(i: Int, pojo: Pojo)

class DefMacroCoderSpec extends AnyFlatSpec with Matchers {

  behavior of "DefMacroCoder"

  it should "generate a coder and serialize/deserialize properly the value." in {
    import DefMacroCoder.{intCoder, stringCoder, listCoder}
    val coder: Coder[Pojo] = DefMacroCoder.productCoder[Pojo]

    val pojo = Pojo(s = "4", i = 1, l = List(2, 3), s2 = "5")

    val encoded: Array[Byte] = {
      val os = new ByteArrayOutputStream()
      coder.encode(pojo, os)
      os.toByteArray
    }
    val decoded: Pojo = coder.decode(new ByteArrayInputStream(encoded))

    pojo shouldBe decoded
  }

  it should "generate a coder for nested case class and serialize/deserialize properly the value." in {
    import DefMacroCoder.{intCoder, stringCoder, listCoder}
    implicit val pojoCoder: Coder[Pojo] = DefMacroCoder.productCoder[Pojo]
    val coder: Coder[ContainsNested] = DefMacroCoder.productCoder[ContainsNested]

    val pojo = ContainsNested(i = 7, pojo = Pojo(s = "4", i = 1, l = List(2, 3), s2 = "5"))

    val encoded: Array[Byte] = {
      val os = new ByteArrayOutputStream()
      coder.encode(pojo, os)
      os.toByteArray
    }
    val decoded: ContainsNested = coder.decode(new ByteArrayInputStream(encoded))

    pojo shouldBe decoded
  }

}
