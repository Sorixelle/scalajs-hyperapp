package cf.srxl.hyperapp

import cf.srxl.hyperapp.Implicits.Dict
import cf.srxl.hyperapp.Tags._

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom._

@JSExportTopLevel("ComponentTest")
object ComponentTest {
  @JSExport
  def run(): Unit = {
    class AppState extends js.Object
    class AppStateConverter extends StateConverter[AppState] {
      override def fromJS(obj: Dict): AppState = new AppState
    }
    implicit val sc: AppStateConverter = new AppStateConverter

    def TestComponent(message: String, children: js.Any*): ViewNode = div(Map(),
      h1(Map("id" -> "message"), message),
      h3(Map(), "Other Children"),
      div(Map(), children: _*)
    )

    val view = (_: AppState) => div(Map(),
      TestComponent("Hello, World!",
        p(Map("id" -> "compChild"), "Test Message")
      )
    )

    new Hyperapp[AppState](
      new ActionResult(new AppState),
      view,
      (_: AppState) => List(),
      document.getElementById("app")
    ).run()
  }

}
