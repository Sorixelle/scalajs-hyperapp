package cf.srxl.hyperapp

import DSL._

import scala.scalajs.js
import org.scalajs.dom._
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("AppMountTest")
object AppMountTest {
  @JSExport
  def run(): Unit = {
    class AppState extends js.Object
    class AppStateConverter extends StateConverter[AppState] {
      override def fromJS(obj: Dict): AppState = new AppState()
    }

    implicit val sc: AppStateConverter = new AppStateConverter()

    new Hyperapp[AppState](
      new ActionResult(new AppState),
      _ => <("p", ^("id" -> "message"), "Hello, World!"),
      document.getElementById("app")
    ).run()
  }
}
