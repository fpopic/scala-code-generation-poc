package com.fpopic.dottymacros


object A:
  def a = 2

object Main {

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    println(msg)
  }

  def msg = "I was compiled by dotty :)"

}
