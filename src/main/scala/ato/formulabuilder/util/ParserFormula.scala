package ato.formulabuilder.util

import scala.util.parsing.combinator.JavaTokenParsers

class ParserFormula extends JavaTokenParsers {

  def parse(formula: String): Option[(Int, Int, Int) => Boolean] = {
    parseAll(expr, formula) match {
      case Success(result, input) => Some(result)
      case Failure(message, input) => None
    }
  }

  // expr  ::= cond1 { "||" cond1 }.
  def expr: Parser[(Int, Int, Int) => Boolean] = cond1 ~ rep("||" ~ cond1) ^^ {
    case c1 ~ cs => (x: Int, y: Int, z: Int) => (c1(x, y, z) :: cs.map({ case "||" ~ c => c(x, y, z)})).contains(true)
  }

  // cond1 ::= cond2 { "&&" cond2 }.
  def cond1: Parser[(Int, Int, Int) => Boolean] = cond2 ~ rep("&&" ~ cond2) ^^ {
    case c1 ~ cs => (x: Int, y: Int, z: Int) => !(c1(x, y, z) :: cs.map({ case "&&" ~ c => c(x, y, z)})).contains(false)
  }

  // cond2 ::= cond3 | "(" expr ")".
  def cond2: Parser[(Int, Int, Int) => Boolean] = cond3 | gl ~> expr <~ gr

  // cond3 ::= expr1 "==" expr1 | expr1 "<" expr1 | expr1 ">" expr1 | expr1 "<=" expr1 | expr1 ">=" expr1.
  def cond3: Parser[(Int, Int, Int) => Boolean] =
    expr1 ~ "==" ~ expr1 ^^ { case e1 ~ "==" ~ e2 => (x: Int, y: Int, z: Int) => e1(x, y, z) == e2(x, y, z)} |
      expr1 ~ "<" ~ expr1 ^^ { case e1 ~ "<" ~ e2 => (x: Int, y: Int, z: Int) => e1(x, y, z) < e2(x, y, z)} |
      expr1 ~ ">" ~ expr1 ^^ { case e1 ~ ">" ~ e2 => (x: Int, y: Int, z: Int) => e1(x, y, z) > e2(x, y, z)} |
      expr1 ~ "<=" ~ expr1 ^^ { case e1 ~ "<=" ~ e2 => (x: Int, y: Int, z: Int) => e1(x, y, z) <= e2(x, y, z)} |
      expr1 ~ ">=" ~ expr1 ^^ { case e1 ~ ">=" ~ e2 => (x: Int, y: Int, z: Int) => e1(x, y, z) >= e2(x, y, z)}

  // expr1 ::= term { "+" term | "-" term }.
  def expr1: Parser[(Int, Int, Int) => Double] = term ~ rep("+" ~ term | "-" ~ term) ^^ { case t1 ~ ts => (x: Int, y: Int, z: Int) => t1(x, y, z) + ts.map({
    case "+" ~ t2 => t2(x, y, z)
    case "-" ~ t2 => -t2(x, y, z)
  }).sum
  }

  // term  ::= factor { "*" factor | "/" factor }.
  def term: Parser[(Int, Int, Int) => Double] = factor ~ rep("*" ~ factor | "/" ~ factor) ^^ { case f1 ~ fs => (x: Int, y: Int, z: Int) => f1(x, y, z) * fs.map({
    case "*" ~ t2 => t2(x, y, z)
    case "/" ~ t2 => 1 / t2(x, y, z)
  }).product
  }

  // factor::= number | coord | "(" expr1 ")" | "|" expr1 "|".
  def factor: Parser[(Int, Int, Int) => Double] =
    floatingPointNumber ^^ (s => (x: Int, y: Int, z: Int) => s.toDouble) |
      coord |
      gl ~> expr1 <~ gr |
      "|" ~ expr1 ~ "|" ^^ { case "|" ~ e ~ "|" => (x: Int, y: Int, z: Int) => math.abs(e(x, y, z))}

  // coord ::= "x" | "y" | "z"
  def coord: Parser[(Int, Int, Int) => Double] = "x" ^^ (_ => (x: Int, y: Int, z: Int) => x.toDouble) |
    "y" ^^ (_ => (x: Int, y: Int, z: Int) => y.toDouble) |
    "z" ^^ (_ => (x: Int, y: Int, z: Int) => z.toDouble)

  def gl: Parser[String] = "(" | "<___"

  def gr: Parser[String] = ")" | "___>"
}
