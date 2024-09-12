/*
rule = DuplicateLiterals
 */
package fix

import java.util.UUID

object DuplicateStringLiterals {
  val hello1: String = "Hello" /* assert: DuplicateLiterals
                       ^^^^^^^
    Literal duplicated within this file
   */

  val world: String = "World"

  val hello2: String = "Hello" /* assert: DuplicateLiterals
                       ^^^^^^^
    Literal duplicated within this file
   */

  val hello3: String = "Hello" /* assert: DuplicateLiterals
                       ^^^^^^^
    Literal duplicated within this file
   */

  val empty1: String = ""
  val empty2: String = ""

  val uuid = UUID.randomUUID()
  val interpolated: String = s"uuid = $uuid"
}
