package com.learning.macros.scalapb._2_Stupid_Way

import com.google.protobuf.CodedOutputStream
import com.learning.macros.scalapb.Person
import com.learning.macros.scalapb._1_TypeClassDef.MessageSerializer

object Option2_BartSimpsonRepetition {

  // 1. way:  different implementation for different type T
  // (Bart Simpson stuff too much)
  // it would be better that we play with things inside T than with every possible T
  new  MessageSerializer[Person] {

    def writeTo(output: CodedOutputStream, person: Person): Unit = {
      output.writeString(1, person.firstName)
      output.writeString(2, person.lastName)
      output.writeInt32(3, person.age)
    }

    def serializedSize(message: Person): Int = ???
  }
}
