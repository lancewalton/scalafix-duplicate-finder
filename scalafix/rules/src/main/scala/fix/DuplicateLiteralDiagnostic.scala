package fix

import scalafix.v1.Diagnostic

import scala.meta.Position

case class DuplicateLiteralDiagnostic(message: String, position: Position)
    extends Diagnostic
