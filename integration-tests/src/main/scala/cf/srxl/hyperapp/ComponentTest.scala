package cf.srxl.hyperapp

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom.document

import DSL._

@JSExportTopLevel("ComponentTest")
object ComponentTest {
  @JSExport
  def run(): Unit = {
    val state: State = Map()
    val actions = Actions()

    val component = Component(_ =>
      <("div", ^("id" -> "component"),
        <("h1", ^(), "Component")
      )
    )

    val componentWithChildren = ComponentWithChildren((_, children) =>
      <("div", ^("id" -> "componentWithChildren"), children:_*)
    )

    val view: View = (_, _) =>
      <("div", ^(),
        <(component, ^()),
        <("hr", ^()),
        <(componentWithChildren, ^(),
          <("h1", ^(), "Component With Children")
        )
      )

    app(state, actions, view, document.getElementById("app"))
  }
}
