package cf.srxl.hyperapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document
import DSL._

import scala.scalajs.js

@JSExportTopLevel("ExternalActionCallTest")
object ExternalActionCallTest {
  var wiredActions: WiredActions = _

  @JSExport
  def run(): Unit = {
    val state: State = Map[String, js.Any](
      "count" -> 0
    )

    val actions = Actions(
      "increment" ==> ((i: Int) => state => state.int("count").map(c => Map("count" -> (c + i))))
    )

    val view: View = (state, _) =>
      <("div", ^(),
        <("h1", ^("id" -> "counter"), state.int("count").get)
      )

    wiredActions = app(state, actions, view, document.getElementById("app"))
  }

  @JSExport
  def incrementCounter(): Unit = wiredActions.action("increment").get(1)
}
