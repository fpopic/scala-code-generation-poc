package com.learning.shapeless.csv._2_IdiomaticFunctional


// The commonly accepted idioma c style for type class defini ons includes a
// companion object containing some standard methods:

trait CsvEncoder[T] {
  def encode(values: T): List[String]
}

object CsvEncoder {

  // "Summoner" or "Materialiser" method to be able to get the materialised encoder
  def apply[T](implicit enc: CsvEncoder[T]): CsvEncoder[T] = enc

  // we can say now just CsvEncoder[IceCream] and we will get that object

  // "Constructor" method
  // The instance method, sometimes named "pure", provides a short nice syntax
  // (check down 2nd booleanEncoder for creating new type class instances,
  // reducing the boilerplate of anonymous class
  // syntax:
  def instance[T](func: T => List[String]): CsvEncoder[T] =
    new CsvEncoder[T] {
      def encode(value: T): List[String] =
        func(value)
    }

  // Custom data type:
  case class Employee(name: String, number: Int, isManager: Boolean)

  // another case class everything same
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  // Globally visible type class instances
  implicit val employeeEncoder: CsvEncoder[Employee] = new CsvEncoder[Employee] {
    def encode(e: Employee): List[String] = List(e.name, e.number.toString, if (e.isManager) "yes" else "no")
  }
  implicit val iceCreamEncoder: CsvEncoder[IceCream] = new CsvEncoder[IceCream] {
    def encode(ic: IceCream) = List(ic.name, ic.numCherries.toString, if (ic.inCone) "yes" else "no")
  }

  //      implicit val booleanEncoder: CsvEncoder[Boolean] =
  //        new CsvEncoder[Boolean] {
  //          def encode(b: Boolean): List[String] =
  //            if(b) List("yes") else List("no")
  //        }

  implicit val booleanEncoder: CsvEncoder[Boolean] =
    instance(b => if (b) List("yes") else List("no"))

}
