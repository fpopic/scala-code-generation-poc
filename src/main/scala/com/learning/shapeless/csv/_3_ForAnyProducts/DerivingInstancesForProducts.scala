package com.learning.shapeless.csv._3_ForAnyProducts

import shapeless.{::, Generic, HList, HNil, Lazy}

trait CsvEncoder[A] {
  def encode(values: A): List[String]
}

object CsvEncoder {

  // "Summoner" or "Materialiser" method to be able to get the materialised encoder
  def apply[T](implicit enc: CsvEncoder[T]): CsvEncoder[T] = enc

  // "constructor" or "pure"
  def constructEncoder[A](func: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      def encode(value: A): List[String] = func(value)
    }

  //0
  implicit val stringEncoder: CsvEncoder[String] = constructEncoder(str => List(str))
  implicit val intEncoder: CsvEncoder[Int] = constructEncoder(num => List(num.toString))
  implicit val booleanEncoder: CsvEncoder[Boolean] = constructEncoder(bool => List(if (bool) "yes" else "no"))

  // We can combine these building blocks to create an encoder for our HList .
  // We’ll use two rules:
  //  1) for HNil and
  //  2) for :: aList shown below:

  //1
  implicit val hnilEncoder: CsvEncoder[HNil] = constructEncoder((_: HNil) => Nil)

  //2
  implicit def hlistEncoder[H, T <: HList](
    implicit
    hEncoder: Lazy[CsvEncoder[H]],
    tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] =
    constructEncoder {
      case h :: t => hEncoder.value.encode(h) ++ tEncoder.encode(t)
    }

  //We mark each instance with the keyword implicit, and define one or more
  //entry point methods that accept an implicit parameter of the corresponding
  //type:
  def writeCsv[T](values: List[T])(implicit enc: CsvEncoder[T]): String =
    values.map(t => enc.encode(t).mkString(",")).mkString("\n")
}


case class IceCream(name: String, numCherries: Int, inCone: Boolean)

object _3_DerivingInstancesForProducts {

  // 1. If we have type class instances for the head and tail of an HList, we
  //    can derive an instance for the whole HList.

  // 2. If we have a case class A, a Generic[A], and a type class instance for
  //    the generic’s Repr, we can combine them to create an instance for A.

  // IceCream has a generic Repr of type String :: Int :: Boolean :: HNil.

  //The Repr is made up of a String, an Int, a Boolean, and an HNil.
  //  1. If we have CsvEncoders for these types, we can create an encoder for the whole thing.
  //  2. If we can derive a CsvEncoder for the Repr, we can create one for IceCream.

  def main(args: Array[String]): Unit = {

    // implicitly -> For summoning implicit lines from the nether world
    // like CsvEncoder.apply (start of a search)
    val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = implicitly

    //Taken together, these five instances allow us to summon CsvEncoders for any
    //HList involving Strings, Ints , and Booleans:
    println(reprEncoder.encode("abc" :: 123 :: true :: HNil))

    // Instances for concrete products (case classes, not just puzzles)

    // We can combine our deriva on rules for HLists with an instance of Generic
    //  to produce a CsvEncoder for IceCream:
    import CsvEncoder._

    implicit val iceCreamEncoder: CsvEncoder[IceCream] = {
      val gen = Generic[IceCream]
      val enc = CsvEncoder[gen.Repr] // lazy doesn't work here
      constructEncoder((iceCream: IceCream) => enc.encode(gen.to(iceCream)))
    }

    // to use generic things for any case class (product type)

    // Given:
    //    a type A,
    //    an HList type R,
    //    an implicit Generic to map A to R,
    //    and a CsvEncoder for R
    // create a CsvEncoder for A.

    //    implicit def genericEncoder[A, R](implicit
    //      gen: Generic[A] {type Repr = R}, // Generic[IceCream] (shapeless macro creates A<=>R
    //      enc: Lazy[CsvEncoder[R]] // CsvCoder2[String :: Int :: Boolean :: HNil]
    //    ): CsvEncoder[A] =
    //      constructEncoder(a => enc.value.encode(gen.to(a))) // return me a CSVCoder2[IceCream]

    // or to simplfy

    implicit def genericEncoder[A, R](implicit
      gen: Generic.Aux[A, R],
      env: Lazy[CsvEncoder[R]]
    ): CsvEncoder[A] =
      constructEncoder(a => env.value.encode(gen.to(a)))


  }
}
