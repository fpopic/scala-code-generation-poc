package com.github.fpopic.reflect

import com.github.fpopic.reflect.OneParamWithExprPrintlnQuasiQuotes.helloDebugImpl

import scala.language.experimental.macros

object ToTableRow {

  def helloDebug(param: Any): Unit = macro helloDebugImpl


}
