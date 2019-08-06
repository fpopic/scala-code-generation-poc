
object Macros{

  import scala.quoted._

  inline def power(x:Long, inline n:Long): Long = ${ powerExpr('x, n) }
      
  def powerExpr(x: Expr[Long], n: Long): Expr[Long] = {
    if (n == 0) 
      '{ 1L } 
    else if (n % 2 == 1)
      '{ $x * ${ powerExpr(x, n - 1) } }
    else '{
      val y: Long = $x * $x
      ${ powerExpr('y, n / 2) }
    }
  }

}