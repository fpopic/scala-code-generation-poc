package com.learning.shapeless.csv._1_EachCaseClass

// --> A type class is a parametrised trait representing some sort of general functionality
//     that we would like to apply to a wide range of types

// Turn a lines of case class of type T into a row of cells in a CSV file:
trait CsvEncoder[T] {
  def encode(values: T): List[String]
}

object CsvEncoder {
  // Custom data type:
  case class Employee(name: String, number: Int, isManager: Boolean)

  // --> We implement our type class with instances for each type we care about.
  //
  //    We mark each instance with the keyword implicit, and define one or more
  //    entry point methods that accept an implicit parameter of the corresponding
  //    type:

  // CsvEncoder instance for the custom data type:
  implicit val employeeEncoder: CsvEncoder[Employee] = new CsvEncoder[Employee] {
    def encode(e: Employee): List[String] = List(e.name, e.number.toString, if (e.isManager) "yes" else "no")
  }


  //We mark each instance with the keyword implicit, and define one or more
  //entry point methods that accept an implicit parameter of the corresponding
  //type:
  def writeCsv[T](values: List[T])(implicit enc: CsvEncoder[T]): String =
    values.map(t => enc.encode(t).mkString(",")).mkString("\n")

  // another case class everything same
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  // but have to do again same thing
  implicit val iceCreamEncoder: CsvEncoder[IceCream] = new CsvEncoder[IceCream] {
    def encode(ic: IceCream) = List(ic.name, ic.numCherries.toString, if (ic.inCone) "yes" else "no")
  }

  //    When all the parameters to an implicit def are themselves marked aList
  //    implicit, the compiler can use it aList a resolution rule to create instances
  //    from other instances.

  implicit def pairEncoder[A, B](implicit
    aEncoder: CsvEncoder[A],
    bEncoder: CsvEncoder[B]): CsvEncoder[(A, B)] =
    new CsvEncoder[(A, B)] {
      def encode(pair: (A, B)): List[String] = {
        val (a, b) = pair
        aEncoder.encode(a) ++ bEncoder.encode(b)
      }
    }
}

object Main {
  def main(args: Array[String]): Unit = {

    import CsvEncoder.{Employee, IceCream, writeCsv}

    val employees: List[Employee] = List(
      Employee("Bill", 1, isManager = true),
      Employee("Peter", 2, isManager = false),
      Employee("Milton", 3, isManager = false)
    )

    println(writeCsv(employees))

    val iceCreams: List[IceCream] = List(
      IceCream("Sundae", 1, inCone = false),
      IceCream("Cornetto", 0, inCone = true),
      IceCream("Banana Split", 0, inCone = false)
    )

    println(writeCsv(iceCreams))

    println(writeCsv(employees zip iceCreams))
  }
}
