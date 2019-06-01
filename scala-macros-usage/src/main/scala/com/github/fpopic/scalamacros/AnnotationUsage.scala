package com.github.fpopic.scalamacros

//@identity
object MyObject {

  @identity
  case class MyCaseClass(x: Int, y: List[String])

//  @identity
  val myVal = 1

}

//@identity
class MyClass(_c: Int) {

  val c: Int = _c

}


@identity
case class MyCaseClass2(x: Int, y: List[String])
