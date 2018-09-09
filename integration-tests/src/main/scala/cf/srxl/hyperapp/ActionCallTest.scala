package cf.srxl.hyperapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document

import DSL._

@JSExportTopLevel("ActionCallTest")
object ActionCallTest {
  @JSExport
  def run(): Unit = {
    val state: State = Map(
      "counter" -> 0
    )

    val actions = Actions(
      "up"   ==> ((i: Int) => state => state.int("counter").map(c => Map("counter" -> (c + i)))),
      "down" ==> ((i: Int) => state => state.int("counter").map(c => Map("counter" -> (c - i))))
    )

    val view: View = (s, a) =>
      <("div", ^(),
        <("h1", ^("id" -> "counter"), s.int("counter").get.toString),
        <("button", ^("id" -> "increment", "onclick" -> (() => a.action("up").get(1))), "+"),
        <("button", ^("id" -> "decrement", "onclick" -> (() => a.action("down").get(1))), "-")
      )

    app(state, actions, view, document.getElementById("app"))
  }
}
