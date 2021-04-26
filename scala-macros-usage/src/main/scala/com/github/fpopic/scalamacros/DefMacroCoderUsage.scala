package com.github.fpopic.scalamacros

import com.github.fpopic.scalamacros.beam.DefMacroCoder

object DefMacroCoderUsage {

  import org.apache.beam.sdk.coders.Coder
  import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

  def main(args: Array[String]): Unit = {

    import com.github.fpopic.scalamacros.beam.DefMacroCoder._
    import com.github.fpopic.scalamacros.beam.DefMacroCoder.{stringCoder, intCoder, listCoder}
    case class Pojo(s: String, i: Int, l: List[Int])
    // TODO try with nested class

    val coder: Coder[Pojo] = DefMacroCoder.of[Pojo]

    val pojo = Pojo("4", 1, List(2, 3))

    val encoded = {
      val os = new ByteArrayOutputStream()
      coder.encode(pojo, os)
      os.toByteArray
    }

    val decoded = coder.decode(new ByteArrayInputStream(encoded))

    require(pojo == decoded)
  }
}
