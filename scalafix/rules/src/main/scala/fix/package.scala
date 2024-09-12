import scala.meta._
import scala.meta.inputs.Input

package object fix {
  def positionToString(position: Position): String = s"${inputToString(position.input)}:${positionLineAndColumnToString(position)}"

  def inputToString(input: Input): String = input match {
    case Input.None => "<Unknown Input>"
    case _: Input.String => "<An Input String>"
    case _: Input.Stream => "<An Input Stream>"
    case Input.File(path, _) => path.toString
    case Input.VirtualFile(path, _) => path.toString
    case _: Input.Slice => "<An Input Slice>"
    case _: Input.Ammonite => "<Ammonite>"
  }

  def positionLineAndColumnToString(position: Position): String = s"${position.startLine + 1}:${position.startColumn + 1}"
}
