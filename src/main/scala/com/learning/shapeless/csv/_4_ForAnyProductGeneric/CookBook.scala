package com.learning.shapeless.csv._4_ForAnyProductGeneric

import shapeless.{::, Generic, HList, HNil, Lazy}


trait CsvEncoder[A] {
  def encode(a: A): List[String]
}

object CsvEncoder {

  // "Summoner" or "Materialiser" is a method that returns the materialised encoder object
  // if someone wants to create just instance of encoder
  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc

  // "constructor" or "pure" (needed for shapeless HList pattern match)
  private def constructEncoder[A](e: A => List[String]): CsvEncoder[A] = a => e(a)

  // primitive encoders
  implicit val stringEncoder: CsvEncoder[String] = constructEncoder(s => List(s))
  implicit val intEncoder: CsvEncoder[Int] = constructEncoder(i => List(i.toString))
  implicit val booleanEncoder: CsvEncoder[Boolean] = constructEncoder(b => List(if (b) "1" else "0"))

  // end of implicit resolution recursion
  implicit val hNilEncoder: CsvEncoder[HNil] = constructEncoder(hNil => List())

  // split implicit resolution to two sparts
  implicit def hListEncoder[HEAD, TAIL <: HList](
    implicit
    hEncoder: Lazy[CsvEncoder[HEAD]],
    tEncoder: CsvEncoder[TAIL]
  ): CsvEncoder[HEAD :: TAIL] =
    constructEncoder {
      case h :: t => hEncoder.value.encode(h) ++ tEncoder.encode(t) // concatenate List(str) ++ List(int) ++ List()
    }

  // for case class A uses shapeless HList r R
  // creates encoder for R
  implicit def genericEncoder[A, R](
    implicit
    gen: Generic.Aux[A, R],
    rEnc: Lazy[CsvEncoder[R]]
  ): CsvEncoder[A] =
  // encoder is a object that can do A => List[String]
    constructEncoder(a => {
      val r = gen.to(a)
      rEnc.value.encode(r)
    })
}

case class MyClass1(str: String, int: Int)
case class MyClass2(boolean: Boolean, str: String)
case class MyClass3(string: String, boolean: Boolean, int: Int)

object Main {

  def main(args: Array[String]): Unit = {

    // this method will start implicit resolution search for Encoder of our case class A
    def writeCsv[A](aList: List[A])(implicit enc: CsvEncoder[A]): String = {
      // map each object of A to csv encoded row (which is List(str, int, bool))
      // and then just convert list to string separated with comma
      val csvRows = aList.map(a => enc.encode(a).mkString(","))
      csvRows.mkString("\n")
    }

    val csvRows = writeCsv(List(MyClass1("John", 42)))
    val csvROws2 = writeCsv(List(MyClass2(false, "Doe")))

    println(csvRows)
    println(csvROws2)


    val myClassEncoder = CsvEncoder[MyClass3]
    println(myClassEncoder.encode(MyClass3("a", false, 1)))
  }

}
