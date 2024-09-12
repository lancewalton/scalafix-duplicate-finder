/*
rule = DuplicateLiterals
 */
package fix

object DuplicateIntegralLiterals {
  val twoByte: Byte = 2 /* assert: DuplicateLiterals
                      ^
    Literal duplicated within this file
   */

  val twoShort: Short = 2 /* assert: DuplicateLiterals
                        ^
    Literal duplicated within this file
   */

  val twoInt: Long = 2 /* assert: DuplicateLiterals
                     ^
    Literal duplicated within this file
   */

  val twoLong: Long = 2L /* assert: DuplicateLiterals
                      ^^
    Literal duplicated within this file
   */

  val singletonByte: Byte = 3
  val singletonShort: Byte = 4
  val singletonInt: Byte = 5
  val singletonLong: Byte = 6

  val minus1Byte: Byte = -1
  val zeroByte: Byte = 0
  val oneByte: Byte = 1

  val minus1Short: Short = -1
  val zeroShort: Short = 0
  val oneShort: Short = 1

  val minus1Int: Int = -1
  val zeroInt: Short = 0
  val oneInt: Short = 1

  val minus1Long: Long = -1
  val zeroLong: Long = 0
  val oneLong: Long = 1
}
