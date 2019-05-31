package com.learning.shapeless.csv._4_ForAnyProductOrCoProductGeneric

import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy, T}


// Given a set of rules encoded as implicit vals and implicit defs, the compiler is
//  capable of searching for combinations to give it the required instances.
// This behaviour, known as “implicit resolution”, is what makes the type class
//  pattern so powerful in Scala.
// Even with this power, the compiler can’t pull apart our case classes and sealed
//  traits. We are required to define instances for ADTs by hand. Shapeless’
//  generic representations change all of this, allowing us to derive instances for
//  any ADT for free.

trait CsvEncoder[A] {
  def encode(a: A): List[String]
}

object CsvEncoder {

  // "Summoner" or "Materialiser" is a method that returns the materialised encoder object
  // if someone wants to create just instance of encoder
  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc

  // "constructor" or "pure" (to avoid always making anonymous class)
  private def constructEncoder[A](e: A => List[String]): CsvEncoder[A] = a => e(a)

  // primitive encoders
  implicit val stringEncoder: CsvEncoder[String] = constructEncoder(s => List(s))
  implicit val intEncoder: CsvEncoder[Int] = constructEncoder(i => List(i.toString))
  implicit val booleanEncoder: CsvEncoder[Boolean] = constructEncoder(b => List(if (b) "1" else "0"))

  // end of implicit resolution recursion
  implicit val hNilEncoder: CsvEncoder[HNil] = constructEncoder(hNil => List())

  // split implicit resolution to two sparts
  implicit def hConsEncoder[HEAD, TAIL <: HList](
    implicit
    hEncoder: Lazy[CsvEncoder[HEAD]],
    tEncoder: CsvEncoder[TAIL]
  ): CsvEncoder[HEAD :: TAIL] =
    constructEncoder {
      case h :: t => hEncoder.value.encode(h) ++ tEncoder.encode(t) // concatenate List(str) ++ List(int) ++ List()
    }

  // move forward with implicit resolution in one direction (either in left or right)
  // In general coproducts take the form A :+: B :+: C :+: CNil meaning
  //  “A or B or C”, where :+: can be loosely interpreted as Either. The overall type
  //  of a coproduct encodes all the possible types in the disjunction on, but each con-
  //  crete instance contains a value for just ONE of the possibilities.
  implicit def cConsEncoder[HEAD, TAIL <: Coproduct](
    implicit
    hEncoder: Lazy[CsvEncoder[HEAD]],
    tEncoder: CsvEncoder[TAIL]
  ): CsvEncoder[HEAD :+: TAIL] =
    constructEncoder {
      case Inl(h) => hEncoder.value.encode(h)
      case Inr(t) => tEncoder.encode(t)
    }


  // implicit def genericEncoder[A](
  //    implicit
  //    gen: Generic[A],
  //    enc: CsvEncoder[gen.Repr] You can't reference a type/val in one param from another enc = ... gen.Repr

  // so move the common thing outside of both params and put it as a type parameter for the method
  // genericEncoder[A, R] and in trait define
  // type Aux[A, Repr0] = Generic[A] { type Repr = Repr0 }

  // for case class A uses shapeless HList's representation R
  // and creates encoder for R
  implicit def genericEncoder[A, R](
    implicit
    gen: Generic.Aux[A, R],
    rEnc: Lazy[CsvEncoder[R]]
  ): CsvEncoder[A] =
  // encoder is just an object that can do A => List[String]
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


