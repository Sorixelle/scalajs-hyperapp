package cf.srxl.hyperapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document

import cf.srxl.hyperapp.DSL._

@JSExportTopLevel("AppMountTest")
object AppMountTest {
  @JSExport
  def run(): Unit = {
    val state: State = Map()
    val actions: Actions = Actions()
    val view: View = (_, _) =>
      <("p", ^("id" -> "message"), "Hello, World!")

    app(state, actions, view, document.getElementById("app"))
  }
}
