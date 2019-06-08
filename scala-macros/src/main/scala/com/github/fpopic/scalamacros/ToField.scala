package com.github.fpopic.scalamacros

import org.joda.time.{DateTimeZone, DateTime}

trait ToField[I, O] {

  def toField(i: I): O

}

object ToField {

  // can generate macro for all primitive types that are boilerplate (key, value)

  implicit def stringToField: ToField[String, String] = s => s

  implicit def intToField: ToField[Int, Int] = i => i

  implicit def intToTimestampLiteral: ToField[Int, String] =
    seconds => new DateTime(seconds * 1000L, DateTimeZone.UTC).toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")

}


// @Timestamp(seconds)
