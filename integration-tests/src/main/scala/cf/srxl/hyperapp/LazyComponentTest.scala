package cf.srxl.hyperapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document

import DSL._

@JSExportTopLevel("LazyComponentTest")
object LazyComponentTest {
  @JSExport
  def run(): Unit = {
    val state: State = Map(
      "count1" -> 0,
      "count2" -> 0
    )

    val actions = Actions(
      "increment1" ==> ((i: Int) => state => state.int("count1").map(c => Map("count1" -> (c + i)))),
      "increment2" ==> ((i: Int) => state => state.int("count2").map(c => Map("count2" -> (c + i))))
    )

    val lazyComponent = LazyComponent(_ => (state, actions) =>
      <("div", ^(),
        <("h1", ^(), "Lazy Counter"),
        <("h1", ^("id" -> "count1"), state.int("count1").get),
        <("button", ^("id" -> "inc1", "onclick" -> (() => actions.action("increment1").get(1))), "+")
      )
    )

    val lazyComponentWithChildren = LazyComponentWithChildren((_, children) => (state, actions) =>
      <("div", ^(), children ++ Seq(
        <("h1", ^("id" -> "count2"), state.int("count2").get),
        <("button", ^("id" -> "inc2", "onclick" -> (() => actions.action("increment2").get(1))), "+")
      ):_*)
    )

    val view: View = (state, actions) =>
      <("div", ^(),
        <(lazyComponent, ^()),
        <("hr", ^()),
        <(lazyComponentWithChildren, ^(),
          <("h1", ^("id" -> "lazyComponentHeading"), "Lazy Counter with Children")
        )
      )

    app(state, actions, view, document.getElementById("app"))
  }
}
