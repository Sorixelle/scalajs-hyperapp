package cf.srxl.hyperapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document

import DSL._

@JSExportTopLevel("NestedStateTest")
object NestedStateTest {
  @JSExport
  def run(): Unit = {
    val state: State = Map(
      "counter" ::> (
        "count" -> 0
      )
    )

    val actions = Actions(
      "counter" :> (
        "increment" ==> ((i: Int) => state => state.int("count").map(c => Map("count" -> (c + i))))
      )
    )

    val view: View = (state, actions) =>
      <("div", ^(),
        <("h1", ^("id" -> "counter"), (for {
          counter <- state.scope("counter")
          count <- counter.int("count")
        } yield count).get),
        <("button", ^("id" -> "inc", "onclick" -> (() => for {
          counter <- actions.scope("counter")
          increment <- counter.action("increment")
        } yield increment(1))), "+")
      )

    app(state, actions, view, document.getElementById("app"))
  }
}
