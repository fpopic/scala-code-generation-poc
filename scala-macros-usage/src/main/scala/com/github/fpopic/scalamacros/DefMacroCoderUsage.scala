package com.github.fpopic.scalamacros

import org.apache.beam.sdk.coders.Coder

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

case class Pojo(i: Int, s: Option[Int], l: List[Int])

object DefMacroCoderUsage {

  def main(args: Array[String]): Unit = {

    import com.github.fpopic.scalamacros.beam.DefMacroCoder._

    val coder: Coder[Pojo] = implicitly

    val pojo = Pojo(1, Some(2), List(3, 4))

    val encoded = {
      val os = new ByteArrayOutputStream()
      coder.encode(pojo, os)
      os.toByteArray
    }

    val decoded = coder.decode(new ByteArrayInputStream(encoded))

    require(pojo == decoded)
  }
}
