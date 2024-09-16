/*
rule = DuplicateFuzzyStringLiterals
 */
package fix

import java.util.UUID

object FuzzyStringLiterals {
  val goodbye1: String = "Goodbye" /* assert: DuplicateFuzzyStringLiterals
                         ^^^^^^^^^
    Literal string fuzzy duplicated within this file
   */

  val goodbye2: String = "Goodbie" /* assert: DuplicateFuzzyStringLiterals
                         ^^^^^^^^^
    Literal string fuzzy duplicated within this file
   */

  val goodbye3: String = "Godbye" /* assert: DuplicateFuzzyStringLiterals
                         ^^^^^^^^
    Literal string fuzzy duplicated within this file
   */

  val empty1: String = ""
  val empty2: String = ""

  val uuid = UUID.randomUUID()
  val interpolated: String = s"uuid = $uuid"
  val interpolated2: String = s"uuid = $uuid"
}
