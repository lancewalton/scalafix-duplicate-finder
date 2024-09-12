/*
rule = DuplicateStringLiterals
 */
package fix

object DetectDuplicateStringLiterals {
  val hello1: String = "Hello" /* assert: DuplicateStringLiterals
                       ^^^^^^^
    Literal string duplicated within this file
   */

  val world: String = "World"

  val hello2: String = "Hello" /* assert: DuplicateStringLiterals
                       ^^^^^^^
    Literal string duplicated within this file
   */

  val hello3: String = "Hello" /* assert: DuplicateStringLiterals
                       ^^^^^^^
    Literal string duplicated within this file
   */

  val empty1: String = ""
  val empty2: String = ""

  val x: Int = 2
  val interpolated: String = s"x = $x"
}
