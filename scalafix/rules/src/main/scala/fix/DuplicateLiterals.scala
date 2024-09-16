package fix

import scalafix.v1._

import scala.collection.compat._
import scala.meta._

case class DuplicateLiteralDiagnostic(message: String, position: Position)
  extends Diagnostic

object DuplicateLiterals {
  private val globalLiterals = scala.collection.mutable.Set.empty[Any]
}

class DuplicateLiterals extends SemanticRule("DuplicateLiterals") {
  import DuplicateLiterals.globalLiterals

  override def fix(implicit doc: SemanticDocument): Patch = {
    val literals = doc.tree
      .collect {
        case lit @ Lit.Int(value) if isInterestingLong(value.toLong) =>
          (value, lit.pos)
        case lit @ Lit.Long(value) if isInterestingLong(value) =>
          (value, lit.pos)
        case lit @ Lit.Float(value) if isInterestingDouble(value.toDouble) =>
          (value, lit.pos)
        case lit @ Lit.Double(value) if isInterestingDouble(value.toDouble) =>
          (value, lit.pos)
        case lit @ Lit.String(value) if isInterestingString(lit) =>
          (value, lit.pos)
      }
      .groupMap(_._1)(_._2)

    val diagnostics = literals.toList.flatMap { case (value, positions) =>
      if (globalLiterals(value))
        if (positions.size > 1)
          positions.map(p =>
            DuplicateLiteralDiagnostic(
              "Literal duplicated within this file and in other files",
              p
            )
          )
        else
          positions.map(p =>
            DuplicateLiteralDiagnostic(
              "Literal duplicated in other files",
              p
            )
          )
      else if (positions.size > 1)
        positions.map(p =>
          DuplicateLiteralDiagnostic(
            "Literal duplicated within this file",
            p
          )
        )
      else
        Nil
    }

    literals.keySet.foreach(globalLiterals.add)

    diagnostics.map(Patch.lint).asPatch
  }

  private def isInterestingDouble(value: Double): Boolean =
    value != -1d &&
      value != 0d &&
      value != 1d

  private def isInterestingLong(value: Long): Boolean =
    value != -1L &&
      value != 0L &&
      value != 1L

  private def isInterestingString(lit: Lit.String): Boolean =
    lit.value.nonEmpty && !isInInterpolatedString(lit)

  private def isInInterpolatedString(str: Lit.String): Boolean =
    str.parent
      .fold(false) {
        case _: Term.Interpolate => true
        case _                   => false
      }
}
