
// from stucki's slides example

object Macros {

  import scala.quoted._

  inline def power(x:Long, inline n:Long): Long = ${ powerExprImpl('x, n) }
      
  def powerExprImpl(x: Expr[Long], n: Long) given (qctx: QuoteContext): Expr[Long] = {
    if (n == 0) 
      '{ 1L } 
    else if (n % 2 == 1)
      '{ $x * ${ powerExprImpl(x, n - 1) } }
    else '{
      val y: Long = $x * $x
      ${ powerExprImpl('y, n / 2) }
    }
  }

}


// from dotty Macros page example

// object MacrosDocsExample {
//   import scala.quoted._

//   inline def assert(expr: => Boolean): Unit = ${ assertImpl('expr) }

//   def assertImpl(expr: Expr[Boolean]) given (qctx: QuoteContext) = '{
//     if (!$expr) 
//       throw new AssertionError(s"failed assertion: ${${ showExpr(expr) }}")
//   }

//   def showExpr(expr: Expr[Boolean]) given (qctx: QuoteContext): Expr[String] =
//     '{ "<some source code>" } // Better implementation later in this document
// }
