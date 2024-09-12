package fix

import scalafix.v1._

import scala.collection.compat._
import scala.meta._

case class DuplicateStringLiteralDiagnostic(message: String, position: Position)
    extends Diagnostic

object DuplicateStringLiterals {
  private val stringLiteralsGlobal = scala.collection.mutable.Set.empty[String]
}

class DuplicateStringLiterals extends SemanticRule("DuplicateStringLiterals") {
  import DuplicateStringLiterals.stringLiteralsGlobal

  override def fix(implicit doc: SemanticDocument): Patch = {
    val stringLiterals = doc.tree
      .collect {
        case lit @ Lit.String(value)
            if value.nonEmpty && !isInInterpolatedString(lit) =>
          lit
      }
      .groupMap(_.value)(_.pos)

    val diagnostics = stringLiterals.toList.flatMap { case (value, positions) =>
      if (stringLiteralsGlobal(value))
        if (positions.size > 1)
          positions.map(p =>
            DuplicateStringLiteralDiagnostic(
              "Literal string duplicated within this file and in other files",
              p
            )
          )
        else
          positions.map(p =>
            DuplicateStringLiteralDiagnostic(
              "Literal string duplicated in other files",
              p
            )
          )
      else if (positions.size > 1)
        positions.map(p =>
          DuplicateStringLiteralDiagnostic(
            "Literal string duplicated within this file",
            p
          )
        )
      else
        Nil
    }

    stringLiterals.keySet.foreach(stringLiteralsGlobal.add)

    diagnostics.map(Patch.lint).asPatch
  }

  private def isInInterpolatedString(str: Lit.String): Boolean =
    str.parent
      .fold(false) {
        case _: Term.Interpolate => true
        case _                   => false
      }
}
