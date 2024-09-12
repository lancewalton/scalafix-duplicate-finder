/*
rule = DuplicateLiterals
 */
package fix

object DuplicateFloatingPointLiterals {
  val twoFloat: Float = 2f /* assert: DuplicateLiterals
                        ^^
    Literal duplicated within this file
   */

  val twoDouble: Double = 2d /* assert: DuplicateLiterals
                          ^^
    Literal duplicated within this file
   */

  val singletonFloat: Float = 3f
  val singletonDouble: Double = 4d

  val minus1Float: Float = -1f
  val zeroFloat: Float = 0f
  val oneFloat: Float = 1f

  val minus1Double: Double = -1d
  val zeroDouble: Double = 0d
  val oneDouble: Double = 1d
}
