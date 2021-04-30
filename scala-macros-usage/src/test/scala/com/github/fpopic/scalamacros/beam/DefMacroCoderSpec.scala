package com.github.fpopic.scalamacros.beam

import org.apache.beam.sdk.coders.Coder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

case class Pojo(s: String, i: Int, l: List[Int])

class DefMacroCoderSpec extends AnyFlatSpec with Matchers {

  behavior of "DefMacroCoder"

  it should "generate a coder and serialize/deserialize properly the value." in {
    import DefMacroCoder.{intCoder, stringCoder, listCoder}
    val coder: Coder[Pojo] = DefMacroCoder.productCoder[Pojo]

    val pojo = Pojo(s = "4", i = 1, l = List(2, 3))

    val encoded: Array[Byte] = {
      val os = new ByteArrayOutputStream()
      coder.encode(pojo, os)
      os.toByteArray
    }
    val decoded: Pojo = coder.decode(new ByteArrayInputStream(encoded))

    pojo shouldBe decoded
  }

}
