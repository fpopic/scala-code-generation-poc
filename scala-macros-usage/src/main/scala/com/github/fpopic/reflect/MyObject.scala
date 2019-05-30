package com.github.fpopic.reflect

@identity
object MyObject {

  @identity
  case class MyCaseClass(x: Int, y: List[String])

  @identity
  val myVal = 1

}

@identity
class MyClass(_c: Int) {

  val c: Int = _c

}
