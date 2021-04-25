package com.github.fpopic.scalamacros.scalapb

import com.google.protobuf.CodedOutputStream


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

  implicit val intSerializer: MessageSerializer[Int] = new MessageSerializer[Int] {
    def writeTo(output: CodedOutputStream, i: Int): Unit = output.writeInt32(1, i)

    def serializedSize(message: Int): Int = 4
  }

  implicit val boolSerializer: MessageSerializer[Boolean] = new MessageSerializer[Boolean] {
    def writeTo(output: CodedOutputStream, b: Boolean): Unit = output.writeBool(2, b)

    def serializedSize(b: Boolean): Int = 1
  }

  // todo with shapeless I can have field serializer
  // todo with macros I could have full message serializer
}
