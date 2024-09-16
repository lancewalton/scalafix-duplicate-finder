package fix

import com.github.vickumar1981.stringdistance.StringDistance.Levenshtein
import scalafix.v1._

import scala.collection.compat._
import scala.meta._

object DuplicateFuzzyStringLiterals {
  private val globalLiterals = scala.collection.mutable.Set.empty[String]
}

class DuplicateFuzzyStringLiterals
    extends SemanticRule("DuplicateFuzzyStringLiterals") {
  import DuplicateFuzzyStringLiterals.globalLiterals

  override def fix(implicit doc: SemanticDocument): Patch = {
    val literals = doc.tree
      .collect {
        case lit @ Lit.String(value) if isInterestingString(lit) =>
          (value, lit.pos)
      }
      .groupMap(_._1)(_._2)

    val literalsWithoutPositions = literals.keySet
    val diagnostics = literals.toList.flatMap { case (value, positions) =>
      val globalMatch = isMatch(value, globalLiterals)
      val localMatch = isMatch(value, literalsWithoutPositions)

      if (globalMatch && localMatch)
        positions.map(p =>
          DuplicateLiteralDiagnostic(
            "Literal string fuzzy duplicated within this file and in other files",
            p
          )
        )
      else if (globalMatch)
        positions.map(p =>
          DuplicateLiteralDiagnostic(
            "Literal string fuzzy duplicated in other files",
            p
          )
        )
      else if (localMatch)
        positions.map(p =>
          DuplicateLiteralDiagnostic(
            "Literal string fuzzy duplicated within this file",
            p
          )
        )
      else
        Nil
    }

    literals.keySet.foreach(globalLiterals.add)

    diagnostics.map(Patch.lint).asPatch
  }

  private def isMatch(str: String, ss: Iterable[String]): Boolean = {
    ss.exists(s => str != s && Levenshtein.score(str, s) >= 0.7)
  }

  private def isInterestingString(lit: Lit.String): Boolean =
    lit.value.nonEmpty && !isInInterpolatedString(lit)

  private def isInInterpolatedString(str: Lit.String): Boolean =
    str.parent
      .fold(false) {
        case _: Term.Interpolate => true
        case _                   => false
      }
}
