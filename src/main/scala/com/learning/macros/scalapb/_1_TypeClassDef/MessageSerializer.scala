package com.learning.macros.scalapb._1_TypeClassDef

import com.google.protobuf.CodedOutputStream
import shapeless.{::, Generic, HList, HNil, Lazy}

// Type class is a class (group of types), which satisfy some contract defined in a trait
// with addition that such functionality (trait and implementation) can be added without
// any changes to the original code.

// trait
trait MessageSerializer[T] {
  def toByteArray(message: T): Array[Byte] = {
    val size = serializedSize(message)
    val bytes = new Array[Byte](size)
    val output = CodedOutputStream.newInstance(bytes)
    writeTo(output, message)
    bytes
  }

  def writeTo(output: CodedOutputStream, message: T): Unit

  def serializedSize(message: T): Int
}

object MessageSerializer {
  // apply method inside companion object
  // instead of making a new object it runs an implicit search and returns an instance that it founds
  def apply[T](implicit ms: MessageSerializer[T]): MessageSerializer[T] = ms

  implicit val intSerializer = new MessageSerializer[Int] {
    def writeTo(output: CodedOutputStream, i: Int): Unit = output.writeInt32(1, i)

    def serializedSize(message: Int): Int = 4
  }

  implicit val boolSerializer = new MessageSerializer[Boolean] {
    def writeTo(output: CodedOutputStream, b: Boolean): Unit = output.writeBool(2, b)

    def serializedSize(b: Boolean): Int = 1
  }

  implicit val hNilSerializer = new MessageSerializer[HNil] {
    def writeTo(output: CodedOutputStream, hNil: HNil): Unit = {}

    def serializedSize(message: HNil): Int = 0
  }

  implicit def hListSerializer[HEAD, TAIL <: HList](
    implicit
    hSerializer: Lazy[MessageSerializer[HEAD]],
    tSerializer: MessageSerializer[TAIL]
  ): MessageSerializer[HEAD :: TAIL] = new MessageSerializer[HEAD :: TAIL] {
    def writeTo(output: CodedOutputStream, hList: HEAD :: TAIL): Unit = hList match {
      case h :: t =>
        hSerializer.value.writeTo(output, h)
        tSerializer.writeTo(output,)
    }

    def serializedSize(hList: HEAD :: TAIL): Int = hList match {
      case h :: t => hSerializer.value.si
    }

    def genericSerializer[T, R](
      implicit
      gen: Generic[T],
      rSer: Lazy[MessageSerializer[T]]
    ): MessageSerializer[T] = {
      null
    }


  }


}

// todo with shapeless I can have field serializer
// todo with macros I could have full message serializer
